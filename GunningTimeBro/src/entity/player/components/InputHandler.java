package entity.player.components;

public class InputHandler {

    private boolean left, right, up, down, jump, attack;

    public InputHandler() {
        resetAll();
    }

    public void resetAll() {
        left = right = up = down = jump = attack = false;
    }

    // Setters
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setUp(boolean up) { this.up = up; }
    public void setDown(boolean down) { this.down = down; }
    public void setJump(boolean jump) { this.jump = jump; }
    public void setAttack(boolean attack) { this.attack = attack; }

    // Getters
    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public boolean isUp() { return up; }
    public boolean isDown() { return down; }
    public boolean isJump() { return jump; }
    public boolean isAttack() { return attack; }
}