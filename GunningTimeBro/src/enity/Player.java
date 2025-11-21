package enity;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerConstants.*;

public class Player extends Enity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 14;
    private int playerAction = IDLE;
    private boolean left, up, right, down;
    private boolean moving, attacking = false;
    private static final float playerSpeed = 3.0f;
    private boolean flip = false;

    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    public void update() {
        setAnimation();
        updateAnimationTick();
        updatePos();
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (attacking) {
            playerAction = HIT;
        } else if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (startAni != playerAction)
            resetAniTick();
    }

    private void updatePos() {
        moving = false;

        if (left && !right) {
            x -= playerSpeed;
            moving = true;
        } else if (right && !left) {
            x += playerSpeed;
            moving = true;
        }

        if (up && !down) {
            y -= playerSpeed;
            moving = true;
        } else if (down && !up) {
            y += playerSpeed;
            moving = true;
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    public void render(Graphics g) {
        int width = 64;
        int xPos = (int) x;

        if (flip) {
            g.drawImage(animations[playerAction][aniIndex], xPos + width, (int) y, -width, 64, null);
        } else {
            g.drawImage(animations[playerAction][aniIndex], xPos, (int) y, width, 64, null);
        }
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][];

        // Load IDLE sprite sheet and extract frames
        BufferedImage idleSheet = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_IDLE);
        animations[IDLE] = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            animations[IDLE][i] = idleSheet.getSubimage(i * 48, 0, 48, 48);
        }

        // Load RUN sprite sheet and extract frames
        BufferedImage runSheet = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_RUN);
        animations[RUNNING] = new BufferedImage[6];
        for (int i = 0; i < 6; i++) {
            animations[RUNNING][i] = runSheet.getSubimage(i * 48, 0, 48, 48);
        }

        // Load HIT/ATTACK sprite sheet and extract frames
        BufferedImage hitSheet = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_HIT);
        animations[HIT] = new BufferedImage[6];
        for (int i = 0; i < 6; i++) {
            animations[HIT][i] = hitSheet.getSubimage(i * 48, 0, 48, 48);
        }
    }

    // Getters and Setters
    public void resetDirBooleans() {
        up = down = left = right = false;
    }

    public boolean isLeft() { return left; }
    public void setLeft(boolean left) { this.left = left; }

    public boolean isUp() { return up; }
    public void setUp(boolean up) { this.up = up; }

    public boolean isRight() { return right; }
    public void setRight(boolean right) { this.right = right; }

    public boolean isDown() { return down; }
    public void setDown(boolean down) { this.down = down; }

    public void setAttacking(boolean attacking) { this.attacking = attacking; }

    public boolean isFlip() { return flip; }
    public void setFlip(boolean flip) { this.flip = flip; }
}