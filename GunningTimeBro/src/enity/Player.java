package enity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.PlayerConstants.*;

public class Player extends Enity {

    private BufferedImage[] idleFrames;
    private static final int IDLE_FRAME_COUNT = 4;
    private static final String PLAYER_IDLE_PATH = "/Player/idle_frame_%d.png";

    private BufferedImage[] runFrames;
    private static final int RUN_FRAME_COUNT = 6;
    private static final String PLAYER_RUN_PATH = "/Player/run_%d.png";

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 20;
    private int playerAction = IDLE;
    private boolean left, up, right, down;
    private boolean moving = false;
    private static final float playerSpeed = 3.0f;

    public Player(float x, float y) {
        super(x, y);
        importImages();
        loadAnimations();
    }

    private void importImages() {
        // Idle frames
        idleFrames = new BufferedImage[IDLE_FRAME_COUNT];
        for (int i = 0; i < IDLE_FRAME_COUNT; i++) {
            String path = String.format(PLAYER_IDLE_PATH, i + 1);
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is == null) throw new IOException("Resource not found: " + path);
                idleFrames[i] = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Run frames
        runFrames = new BufferedImage[RUN_FRAME_COUNT];
        for (int i = 0; i < RUN_FRAME_COUNT; i++) {
            String path = String.format(PLAYER_RUN_PATH, i + 1);
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is == null) throw new IOException("Resource not found: " + path);
                runFrames[i] = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][];
        animations[IDLE] = idleFrames;
        animations[RUNNING] = runFrames;
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
        updatePosition();
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= animations[playerAction].length) {
                aniIndex = 0;
            }
        }
    }

    private void setAnimation() {
        int previousAction = playerAction;
        playerAction = moving ? RUNNING : IDLE;
        if (previousAction != playerAction) {
            aniIndex = 0;
            aniTick = 0;
        }
    }

    private void updatePosition() {
        moving = false;

        if (left && !right) { x -= playerSpeed; moving = true; }
        if (right && !left) { x += playerSpeed; moving = true; }
        if (up && !down) { y -= playerSpeed; moving = true; }
        if (down && !up) { y += playerSpeed; moving = true; }
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, 64, 64, null);
    }

    public void resetDirBooleans() {
        left = up = right = down = false;
    }

    // Setter & Getter cho KeyboardInputs
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setUp(boolean up) { this.up = up; }
    public void setDown(boolean down) { this.down = down; }
    public boolean isMoving() { return moving; }
}
