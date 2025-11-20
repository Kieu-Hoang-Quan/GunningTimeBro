package enity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.Directions.*;
import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Enity {
    private BufferedImage[] idleFrames;
    private static final int IDLE_FRAME_COUNT = 4;
    private static final String PLAYER_SPRITE_PATH = "/Player/idle_frame_%d.png";

    private BufferedImage[] runFrames;
    private static final int RUN_FRAME_COUNT = 6;
    private static final String RUN_SPRITE_PATH = "/Player/run_%d.png";

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 20;
    private int playerAction = IDLE;
    private boolean left, up, right, down;
    private boolean moving = false;
    private static final float playerSpeed = 3.0f;

    public Player(float x, float y) {
        super(x, y);
        importImg();
        loadAnimations();
    }

    private void importImg() {
        // Idle
        idleFrames = new BufferedImage[IDLE_FRAME_COUNT];
        for (int i = 0; i < IDLE_FRAME_COUNT; i++) {
            String resourcePath = String.format(PLAYER_SPRITE_PATH, i + 1);
            try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new IOException("Resource not found: " + resourcePath);
                }
                idleFrames[i] = ImageIO.read(is);
            } catch (IOException e) {
                System.err.println("Failed to load player sprite frame " + (i + 1) + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        // RUN
        runFrames = new BufferedImage[RUN_FRAME_COUNT];
        for (int i = 0; i < RUN_FRAME_COUNT; i++) {
            String resourcePath = String.format(RUN_SPRITE_PATH, i + 1);
            try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new IOException("Resource not found: " + resourcePath);
                }
                runFrames[i] = ImageIO.read(is);
            } catch (IOException e) {
                System.err.println("Failed to load run sprite frame " + (i + 1) + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][]; // 9 animation states based on Constants

        // Initialize IDLE animation
        animations[IDLE] = new BufferedImage[IDLE_FRAME_COUNT];
        for (int i = 0; i < IDLE_FRAME_COUNT; i++) {
            animations[IDLE][i] = idleFrames[i];
        }

        // Initialize RUNNING animation
        animations[RUNNING] = new BufferedImage[RUN_FRAME_COUNT];
        for (int i = 0; i < RUN_FRAME_COUNT; i++) {
            animations[RUNNING][i] = runFrames[i];
        }

//        // Initialize other animation arrays (empty for now)
//        for (int i = 0; i < animations.length; i++) {
//            if (i != IDLE && i != RUNNING) {
//                int spriteAmount = GetSpriteAmount(i);
//                animations[i] = new BufferedImage[spriteAmount];
//            }
//        }
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
        updatePos();
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
            }
        }
    }


    private void setAnimation() {
        int startAni = playerAction;

        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        // Reset animation when action changes
        if (startAni != playerAction) {
            aniIndex = 0;
            aniTick = 0;
        }
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

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, 64, 64, null);
    }

    public void resetDirBooleans(){
        up = false;
        down = false;
        left = false;
        right = false;
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
}
