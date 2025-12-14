package entity. player;

import entity.Entity;
import entity.components. HealthComponent;
import entity.components.MeleeComponent;
import entity.player.playercomponents.*;
import entity.enemy.Enemy;
import main.Game;
import utilz.PhysicsHelper;
import static utilz.Constants.PlayerConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Player extends Entity {

    private PlayerPhysics physics;
    private PlayerAnimator animator;
    private PlayerRender renderer;

    private InputHandler inputHandler;
    private MovementState movementState;
    private AnimationState animationState;

    private HealthComponent healthComponent;
    private MeleeComponent meleeComponent;

    private PlayerInventory inventory;
    private PlayerAbilitySystem abilitySystem;
    private PlayerBuffSystem buffSystem;

    private boolean flip = false;
    private boolean attacking = false;
    private boolean attackStarted = false;
    private boolean attackHit = false;
    private boolean usingLightning = false;

    private int[][] lvlData;
    private ArrayList<Enemy> enemies;

    private Rectangle2D.Float attackBox;

    private static final float NORMAL_ATTACK_RANGE = 20f * Game.SCALE;
    private static final float LIGHTNING_ATTACK_RANGE = 40f * Game.SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);

        initHitbox(x, y, 6 * Game.SCALE, 15 * Game.SCALE);
        attackBox = new Rectangle2D.Float(x, y, NORMAL_ATTACK_RANGE, 15 * Game.SCALE);

        this.physics = new PlayerPhysics(this);
        this.animator = new PlayerAnimator();
        this.renderer = new PlayerRender();

        this.inputHandler = new InputHandler();
        this.movementState = new MovementState(physics, inputHandler);
        this.animationState = new AnimationState(movementState);

        this.healthComponent = new HealthComponent(this, 100f);
        this.meleeComponent = new MeleeComponent(this, 15f);

        this.inventory = new PlayerInventory();
        this.abilitySystem = new PlayerAbilitySystem();
        this.buffSystem = new PlayerBuffSystem();
    }

    public void update() {
        if (lvlData == null) return;

        if (! healthComponent.isAlive()) {
            return;
        }

        updateAttackState();

        physics.update(lvlData,
                inputHandler.isLeft(),
                inputHandler.isRight(),
                inputHandler.isJump()
        );

        healthComponent.update(1f / 60f);
        meleeComponent.update(1f / 60f);
        buffSystem.update(1f / 60f);

        updateDamageBoosts();

        updateFlip();
        updateAttackBox();

        animationState.setAttacking(attacking);
        animationState.setUsingLightning(usingLightning);
        int currentAction = animationState.getCurrentAction();
        animator.update(currentAction);
    }

    private void updateDamageBoosts() {
        float totalBoost = buffSystem.getTotalBuffValue("DAMAGE_BOOST");
        meleeComponent.setDamageMultiplier(1.0f + totalBoost);
    }

    private void updateAttackState() {
        if (attackStarted) {
            attacking = true;

            if (animator.getAniIndex() == 3 && !attackHit && enemies != null) {
                checkAttackHit();
                attackHit = true;
            }

            if (animator.isAnimationFinished()) {
                attacking = false;
                attackStarted = false;
                attackHit = false;
                usingLightning = false;
                animator.resetAnimation();
            }
        }
    }

    private void checkAttackHit() {
        int attackType = animationState.getCurrentAction();
        float finalDamage = meleeComponent. calculateDamage(attackType);

        System.out.println("========== ATTACK ==========");
        System.out.println("Type: " + getAttackTypeName());
        System.out.println("FINAL DAMAGE: " + finalDamage);
        System.out.println("============================");

        for (Enemy enemy : enemies) {
            if (enemy.isActive() && enemy.getHealthComponent().isAlive()) {
                if (attackBox.intersects(enemy.getHitbox())) {
                    enemy.getHealthComponent().takeDamage(finalDamage, this);
                    meleeComponent.incrementCombo();
                    System.out.println("[Player] Hit!  Combo: " + meleeComponent.getComboCount());
                }
            }
        }
    }

    private String getAttackTypeName() {
        int currentAction = animationState.getCurrentAction();

        switch (currentAction) {
            case LIGHTNING:
                return "LIGHTNING STRIKE";
            case HITRUN:
                return "Running Attack";
            case HIT:
                return "Standing Attack";
            default:
                return "Attack";
        }
    }

    private void updateAttackBox() {
        float range = NORMAL_ATTACK_RANGE;

        if (usingLightning) {
            range = LIGHTNING_ATTACK_RANGE;
        }

        attackBox.width = range;

        if (flip) {
            attackBox.x = hitbox.x - attackBox.width;
        } else {
            attackBox.x = hitbox.x + hitbox.width;
        }
        attackBox.y = hitbox.y;
    }

    private void updateFlip() {
        if (inputHandler.isLeft()) flip = true;
        if (inputHandler.isRight()) flip = false;
    }

    public void render(Graphics g, int camX) {
        renderer.render(g, this, camX);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (! PhysicsHelper.isEntityOnFloor(hitbox, lvlData, Game.TILES_SIZE)) {
            physics.setInAir(true);
        }
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
        physics.setEnemies(enemies);
    }

    public void resetDirBooleans() {
        inputHandler.resetAll();
    }

    public void setAttacking(boolean attacking) {
        if (attacking && !this.attacking && !attackStarted) {
            this.attackStarted = true;
            this.attackHit = false;
            this.usingLightning = false;
            animator.resetAnimation();
        }
    }

    public void useLightningPower() {
        if (attacking || attackStarted) {
            return;
        }

        if (abilitySystem.useLightning()) {
            this.usingLightning = true;
            this.attackStarted = true;
            this.attackHit = false;
            animator.resetAnimation();

            int remaining = abilitySystem.getLightningCharges();
            System.out.println("LIGHTNING STRIKE");
            System.out.println("Charges remaining: " + remaining);
        } else {
            System.out.println("[Lightning] No charges!  Collect Lightning Power items.");
        }
    }

    public HealthComponent getHealthComponent() { return healthComponent; }
    public MeleeComponent getMeleeComponent() { return meleeComponent; }
    public PlayerInventory getInventory() { return inventory; }
    public PlayerAbilitySystem getAbilitySystem() { return abilitySystem; }
    public PlayerBuffSystem getBuffSystem() { return buffSystem; }

    public void setFlip(boolean flip) { this.flip = flip; }
    public boolean isFlip() { return flip; }

    public void setLeft(boolean left) { inputHandler.setLeft(left); }
    public void setRight(boolean right) { inputHandler.setRight(right); }
    public void setJump(boolean jump) { inputHandler.setJump(jump); }
    public void setDown(boolean down) { inputHandler.setDown(down); }

    public PlayerAnimator getAnimator() { return animator; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}