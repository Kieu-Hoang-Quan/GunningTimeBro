package entity;

import Gun.Gun;
import Gun.Bullet;
import main.Game;
import utilz.LoadSave;

import static utilz.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.HelpMethods.CanMoveHere;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 20;
    private int playerAction = IDLE;
    private boolean left, up, right, down, jump;
    private boolean moving, attacking = false;
    private float playerSpeed = 3.0f;
    private boolean flip = false;

    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 25 * Game.SCALE;

    //Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, 6 * Game.SCALE, 15 * Game.SCALE);
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
        if (gun != null) {
            gun.update(1.0 / 200);
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
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

        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if (inAir) {
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }

        if (attacking)
            playerAction = HIT;

        if (startAni != playerAction)
            resetAniTick();
    }


    private void updatePos() {
        moving = false;
        float xSpeed = 0, ySpeed = 0;
        if (jump)
            jump();
        if (!left && !right && !inAir)
            return;

        xSpeed = 0;

        if (left)
            xSpeed -= playerSpeed;
        if (right)
            xSpeed += playerSpeed;

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        } else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;

    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;

    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }

    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    public void render(Graphics g, int camX) {
        int spriteW = width;
        int spriteH = height;

        int drawY = (int) (hitbox.y - yDrawOffset);

        if (flip) {
            // toạ độ world
            int drawXWorld = (int) (hitbox.x + hitbox.width + xDrawOffset);
            // trừ camera ra để ra toạ độ màn hình
            int screenX = drawXWorld - camX;

            g.drawImage(animations[playerAction][aniIndex],
                    screenX,
                    drawY,
                    -spriteW,
                    spriteH,
                    null);
        } else {
            int drawXWorld = (int) (hitbox.x - xDrawOffset);
            int screenX = drawXWorld - camX;

            g.drawImage(animations[playerAction][aniIndex],
                    screenX,
                    drawY,
                    spriteW,
                    spriteH,
                    null);
        }
    }


    private void loadAnimations() {


        animations = new BufferedImage[9][6];

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
        // JUMP
        BufferedImage jumpSheet = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_JUMP);
        animations[JUMP] = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            animations[JUMP][i] = jumpSheet.getSubimage(i * 48, 0, 48, 48);
        }

        // FALLING: Re-use the JUMP animation frames for FALLING
        animations[FALLING] = animations[JUMP];
    }
    private Gun gun;
    public void equipWeapon(Gun g) {
        this.gun = g;
    }
    public Bullet shoot() {
        if (gun == null) return null;

        float bulletX = (float)(hitbox.x + hitbox.width / 2);
        float bulletY = (float)(hitbox.y + hitbox.height / 2);

        return gun.tryFire(bulletX, bulletY, flip);
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

    public void setJump(boolean jump) {
        this.jump = jump;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

}