package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;


public class GamePanel extends JPanel {

    private float xDelta = 100, yDelta = 100;
    private BufferedImage[] idleFrames;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false;
    private static final int IDLE_FRAME_COUNT = 4;
    private static final String PLAYER_SPRITE_PATH = "/Player/idle_frame_%d.png";
    private static final int SPRITE_WIDTH = 128;
    private static final int SPRITE_HEIGHT = 80;


    public GamePanel() {

        importImg();
        loadAnimations();

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));

    }

    private void importImg() {
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
    }
    private void loadAnimations() {
        animations = new BufferedImage[9][]; // 9 animation states based on Constants

        for (int i = 0; i < animations.length; i++) {
            int spriteAmount = GetSpriteAmount(i);
            animations[i] = new BufferedImage[spriteAmount];

            for (int j = 0; j < spriteAmount; j++) {
                if (i == IDLE && j < idleFrames.length) {
                    animations[i][j] = idleFrames[j];
                }
                // Add loading logic for other animation states here when you have the resources
            }
        }
    }


    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setDirection(int direction) {
        this.playerDir = direction;
        moving = true;
    }

    public void changeXDelta(int value) {
        this.xDelta += value;

    }

    public void changeYDelta(int value) {
        this.yDelta += value;

    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= IDLE_FRAME_COUNT) {
                aniIndex = 0;
            }
        }
    }

    private void setAnimation() {
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
    }

    private void updatePos() {
        if (moving) {
            switch (playerDir) {
                case LEFT:
                    xDelta -= 5;
                    break;
                case UP:
                    yDelta -= 5;
                    break;
                case RIGHT:
                    xDelta += 5;
                    break;
                case DOWN:
                    yDelta += 5;
                    break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateAnimationTick();
        setAnimation();
        updatePos();

        if (idleFrames != null && idleFrames.length > 0 && idleFrames[aniIndex] != null) {
            g.drawImage(idleFrames[aniIndex], (int) xDelta, (int) yDelta, SPRITE_WIDTH, SPRITE_HEIGHT, null);
        }
    }
}
