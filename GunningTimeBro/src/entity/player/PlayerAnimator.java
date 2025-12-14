package entity.player;

import static utilz.Constants.PlayerConstants.*;
import utilz.LoadSave;
import java.awt.image.BufferedImage;

public class PlayerAnimator {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int playerAction = IDLE;

    public PlayerAnimator() {
        loadAnimations();
    }

    public void update(int currentAction) {
        if (playerAction != currentAction) {
            playerAction = currentAction;
            resetAnimation();
        }

        updateAnimationTick();
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

    public boolean isAnimationFinished() {
        return aniIndex == GetSpriteAmount(playerAction) - 1 &&
                aniTick >= aniSpeed - 1;
    }

    public void resetAnimation() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][8];

        animations[IDLE] = loadStartParams(LoadSave.PLAYER_IDLE, 4);
        animations[RUNNING] = loadStartParams(LoadSave.PLAYER_RUN, 6);
        animations[HIT] = loadStartParams(LoadSave.PLAYER_HIT, 8);
        animations[JUMP] = loadStartParams(LoadSave.PLAYER_JUMP, 4);
        animations[HITRUN] = loadStartParams(LoadSave.PLAYER_HITRUN, 6);
        animations[FALLING] = animations[JUMP];
        animations[LIGHTNING] = loadStartParams(LoadSave.PLAYER_LIGHTNING, 8);
    }

    private BufferedImage[] loadStartParams(String filename, int frames) {
        BufferedImage sheet = LoadSave.GetSpriteAtlas(filename);

        if (sheet == null) {
            System.err.println("[PlayerAnimator] Failed to load: " + filename);
            return createEmptyArray(frames);
        }

        BufferedImage[] arr = new BufferedImage[frames];
        int frameWidth = sheet.getWidth() / frames;
        int frameHeight = sheet.getHeight();

        for (int i = 0; i < frames; i++) {
            try {
                int x = i * frameWidth;
                if (x + frameWidth > sheet. getWidth()) {
                    System.err.println("[PlayerAnimator] Frame " + i + " out of bounds for " + filename);
                    arr[i] = createPlaceholder(frameWidth, frameHeight);
                    continue;
                }

                arr[i] = sheet. getSubimage(x, 0, frameWidth, frameHeight);

            } catch (Exception e) {
                System.err.println("[PlayerAnimator] Error extracting frame " + i + " from " + filename);
                e. printStackTrace();
                arr[i] = createPlaceholder(frameWidth, frameHeight);
            }
        }
        return arr;
    }

    private BufferedImage[] createEmptyArray(int frames) {
        BufferedImage[] arr = new BufferedImage[frames];
        for (int i = 0; i < frames; i++) {
            arr[i] = createPlaceholder(48, 48);
        }
        return arr;
    }

    private BufferedImage createPlaceholder(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(java.awt.Color. MAGENTA);
        g.fillRect(0, 0, width, height);
        g.setColor(java.awt.Color. YELLOW);
        g.drawString("? ", width/2 - 5, height/2);
        g.dispose();
        return img;
    }

    public BufferedImage getCurrentFrame() {
        if (playerAction < 0 || playerAction >= animations.length) {
            System.err.println("[PlayerAnimator] Invalid action: " + playerAction);
            return animations[IDLE][0];
        }

        if (animations[playerAction] == null) {
            System. err.println("[PlayerAnimator] Null animation for action: " + playerAction);
            return animations[IDLE][0];
        }

        if (aniIndex < 0 || aniIndex >= animations[playerAction].length) {
            System.err.println("[PlayerAnimator] Invalid frame index: " + aniIndex + " for action: " + playerAction);
            return animations[playerAction][0];
        }

        BufferedImage frame = animations[playerAction][aniIndex];
        if (frame == null) {
            System.err.println("[PlayerAnimator] Null frame at index: " + aniIndex);
            return animations[IDLE][0];
        }

        return frame;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}