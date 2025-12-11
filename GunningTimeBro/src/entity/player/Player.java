package entity.player;

import entity.Entity;
import entity.player.playercomponents.*;
import main.Game;

import java.awt.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods. IsEntityOnFloor;

public class Player extends Entity {



    private PlayerPhysics physics;
    private PlayerAnimator animator;
    private PlayerRender renderer;

    private InputHandler inputHandler;
    private MovementState movementState;
    private AnimationState animationState;



    private boolean flip = false;

    // Attack state
    private boolean attacking = false;
    private boolean attackStarted = false;

    private int[][] lvlData;



    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);

        initHitbox(x, y, 6 * Game.SCALE, 15 * Game.SCALE);

        // Initialize components
        this.physics = new PlayerPhysics(this);
        this.animator = new PlayerAnimator();
        this.renderer = new PlayerRender();

        this.inputHandler = new InputHandler();
        this.movementState = new MovementState(physics, inputHandler);
        this.animationState = new AnimationState(movementState);
    }



    public void update() {
        if (lvlData == null) return;

        // Update attack state
        updateAttackState();

        // Update physics
        physics.update(lvlData,
                inputHandler.isLeft(),
                inputHandler.isRight(),
                inputHandler.isJump()
        );

        // Update flip direction
        updateFlip();

        // Update animation state
        animationState.setAttacking(attacking);
        int currentAction = animationState.getCurrentAction();
        animator.update(currentAction);
    }


    private void updateAttackState() {
        if (attackStarted) {
            attacking = true;

            // Check if attack animation finished
            if (animator.isAnimationFinished()) {
                attacking = false;
                attackStarted = false;
                animator.resetAnimation();
            }
        }
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
        if (! IsEntityOnFloor(hitbox, lvlData)) {
            physics.setInAir(true);
        }
    }

    public void resetDirBooleans() {
        inputHandler.resetAll();
    }

    public void setAttacking(boolean attacking) {
        if (attacking && !this.attacking && !attackStarted) {
            this.attackStarted = true;
            animator.resetAnimation();
        }
    }

    public void setFlip(boolean flip) { this.flip = flip; }
    public boolean isFlip() { return flip; }

    public void setLeft(boolean left) { inputHandler.setLeft(left); }
    public void setRight(boolean right) { inputHandler.setRight(right); }
    public void setJump(boolean jump) { inputHandler.setJump(jump); }
    public void setDown(boolean down) { inputHandler.setDown(down); }

    public PlayerAnimator getAnimator() { return animator; }
}