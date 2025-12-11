package entity.player;

import entity.Entity;
import main.Game;
import java.awt.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.IsEntityOnFloor;

public class Player extends Entity {

    // Components (Composition over Inheritance)
    private PlayerPhysics physics;
    private PlayerAnimator animator;
    private PlayerRender renderer;

    // State
    private int playerAction = IDLE;
    private boolean left, up, right, down, jump;
    private boolean moving = false, attacking = false;
    private boolean flip = false;

    // Level Data
    private int[][] lvlData;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);

        // Khởi tạo các components
        initHitbox(x, y, 6 * Game.SCALE, 15 * Game.SCALE);
        this.physics = new PlayerPhysics(this);
        this.animator = new PlayerAnimator();
        this.renderer = new PlayerRender();
    }

    public void update() {
        if (lvlData == null) return;

        // 1. Update Physics (Di chuyển)
        physics.update(lvlData, left, right, jump);

        // 2. Update Logic Game (Quyết định action là gì)
        updateHealthOrGameLogic(); // Ví dụ

        // 3. Quyết định Animation State dựa trên Physics/Input
        setAnimationState();

        // 4. Update Animation tick
        animator.update(playerAction);
    }

    public void render(Graphics g, int camX) {
        renderer.render(g, this, camX);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            physics.setInAir(true);
        }
    }

    private void updateHealthOrGameLogic() {
        // Kiểm tra xem có đang di chuyển không để set cờ moving
        if (left || right) moving = true;
        else moving = false;

        // Xử lý lật hình
        if (left) flip = true;
        if (right) flip = false;
    }

    private void setAnimationState() {
        int startAni = playerAction;

        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if (physics.isInAir()) {
            if (physics.getAirSpeed() < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }

        if (attacking) {
            playerAction = HIT; // Hoặc ATTACK_1
            // Logic reset attack khi hết animation frame sẽ xử lý ở animator hoặc check index
            if (animator.getAniIndex() == 0 && playerAction == HIT) {
                // attacking = false; // Logic này cần tinh chỉnh tùy game
            }
        }
    }

    // --- Getters & Setters ---

    public void resetDirBooleans() {
        up = down = left = right = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }
    public boolean isFlip() { return flip; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setJump(boolean jump) { this.jump = jump; }
    public void setDown(boolean down) { this.down = down; }

    public PlayerAnimator getAnimator() { return animator; }

    public int getWidth() {
        return width;
    }


    public int getHeight() { return height;}
}