package entity;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.HelpMethods.CanMoveHere;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 20;
    private int playerAction = IDLE;
    private boolean left, up, right, down;
    private boolean moving, attacking = false;
    private float playerSpeed = 3.0f;
    private boolean flip = false;

    //Testing
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 25 * Game.SCALE;

    //

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, 6 * Game.SCALE, 15 * Game.SCALE);
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    //Testing
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    //

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
        float xSpeed = 0, ySpeed = 0;
        if (!left && !right && !up && !down)
            return;

        if (left && !right)
            xSpeed = -playerSpeed;
        else if (right && !left)
            xSpeed = playerSpeed;

        if (up && !down)
            ySpeed = -playerSpeed;
        else if (down && !up)
            ySpeed = playerSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
            moving = true;
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    public void render(Graphics g) {
        int spriteW = width;
        int spriteH = height;

        int drawX;
        int drawY = (int) (hitbox.y - yDrawOffset);

        if (flip) {
            // hitbox.x là mép trái hitbox
            // hitbox.width là độ rộng hitbox
            // xDrawOffset = khoảng trống từ trái sprite đến trái hitbox khi nhìn sang phải
            drawX = (int) (hitbox.x + hitbox.width + xDrawOffset); // mép phải sprite
            g.drawImage(animations[playerAction][aniIndex],
                    drawX,
                    drawY,
                    -spriteW,    // width âm để flip
                    spriteH,
                    null);
        } else {
            // bình thường: dùng mép trái sprite
            drawX = (int) (hitbox.x - xDrawOffset);
            g.drawImage(animations[playerAction][aniIndex],
                    drawX,
                    drawY,
                    spriteW,
                    spriteH,
                    null);
        }

//        // Vẽ hitbox để debug: KHÔNG dùng offset
//        drawHitbox(g);
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

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }
}