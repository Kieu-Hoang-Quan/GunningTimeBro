package entity. enemy;

import entity.Entity;
import static utilz.Constants.EnemyConstants.*;
import entity.components.EffectRenderer;
import entity.components. HealthComponent;
import entity.player.Player;

import java.awt.*;
import java. awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity {

    protected EnemyPhysics physics;
    protected EnemyAnimator animator;
    protected EnemyRender renderer;
    protected HealthComponent healthComponent;
    protected EffectRenderer effectRenderer;

    protected int enemyType;
    protected int enemyState = IDLE;
    protected boolean active = true;
    protected boolean flip = false;

    protected int xDrawOffset;
    protected int yDrawOffset;

    protected Rectangle2D.Float attackBox;
    public boolean attackChecked = false;
    public int attackDamage = 20;

    protected float chaseRange = 120f;
    protected float attackTriggerRange = 45f;

    private int deathTimer = 0;
    private static final int DEATH_FADE_TIME = 60;  // 1 second at 60 FPS

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;

        this.physics = new EnemyPhysics(this);
        this.animator = new EnemyAnimator(this);
        this.renderer = new EnemyRender();
        this.healthComponent = new HealthComponent(this, 33f);  //1/3 OF PLAYER (100/3 = 33)
        this.effectRenderer = new EffectRenderer();

        initHitbox(x, y, width - 10, height - 5);
        attackBox = new Rectangle2D.Float(x, y, width, height);
    }

    public void update(int[][] lvlData, Player player) {
        healthComponent.update(1f / 60f);

        if (! healthComponent.isAlive() && enemyState != DEAD) {
            setNewState(DEAD);
        }

        if (enemyState == DEAD) {
            animator.update();
            deathTimer++;

            // DISAPPEAR AFTER FADE
            if (deathTimer >= DEATH_FADE_TIME) {
                active = false;
            }
            return;
        }

        physics.update(lvlData);
        updateStateAI(player);
        updateAttackBox();
        animator.update();
    }

    public void render(Graphics g, BufferedImage[][] sprites, int xOff) {
        renderer.render(g, this, sprites, xOff);
    }

    protected void updateStateAI(Player player) {
        if (enemyState == DEAD) return;

        if (enemyState == HIT) {
            if (animator.getAniIndex() >= GetSpriteAmount(enemyType, HIT) - 1)
                setNewState(RUNNING);
            return;
        }

        float px = player.getHitbox().x;
        float ex = hitbox.x;
        float distX = Math.abs(px - ex);
        float distY = Math.abs(player.getHitbox().y - hitbox.y);
        boolean sameLevel = distY < hitbox.height * 0.6f;

        if (sameLevel && distX < attackTriggerRange) {
            flip = px < ex;
            setNewState(ATTACK);
            return;
        }

        if (sameLevel && distX < chaseRange) {
            flip = px < ex;
            setNewState(RUNNING);
            return;
        }

        setNewState(RUNNING);
    }

    protected void updateAttackBox() {
        float w = hitbox.width;
        float h = hitbox.height;

        float effectiveRange = (enemyState == ATTACK) ? w :  w * 0.6f;

        attackBox.width = effectiveRange;
        attackBox. height = h * 0.5f;
        attackBox.y = hitbox.y + (h - attackBox.height) / 2;

        if (flip) {
            attackBox.x = hitbox. x - attackBox.width + 10;
        } else {
            attackBox.x = hitbox. x + w - 10;
        }
    }

    public void setNewState(int state) {
        if (this.enemyState == state) return;
        this.enemyState = state;
        animator.reset();
        attackChecked = false;
    }

    public boolean isAttackFrame() {
        return animator.getAniIndex() == 3;
    }

    public int getDeathTimer() { return deathTimer; }
    public HealthComponent getHealthComponent() { return healthComponent; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isActive() { return active; }
    public boolean isFlip() { return flip; }
    public void setFlip(boolean flip) { this.flip = flip; }
    public int getEnemyState() { return enemyState; }
    public int getEnemyType() { return enemyType; }
    public EnemyAnimator getAnimator() { return animator; }
    public Rectangle2D.Float getAttackBox() { return attackBox; }
    public int getXDrawOffset() { return xDrawOffset; }
    public int getYDrawOffset() { return yDrawOffset; }
    public void setAttackChecked(boolean checked) { this.attackChecked = checked; }
}