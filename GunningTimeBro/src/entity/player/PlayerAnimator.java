package entity.player;

import utilz.LoadSave;
import java.awt. image.BufferedImage;
import static utilz.Constants.PlayerConstants.*;

public class PlayerAnimator {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int playerAction = IDLE;

    public PlayerAnimator() {
        loadAnimations();
    }

    public void update(int currentAction) {
        // If action changed, reset animation
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
        return aniIndex == GetSpriteAmount(playerAction) - 1 && aniTick >= aniSpeed - 1;
    }

    public void resetAnimation() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][6];

        animations[IDLE] = loadStartParams(LoadSave.PLAYER_IDLE, 4);
        animations[RUNNING] = loadStartParams(LoadSave.PLAYER_RUN, 6);
        animations[HIT] = loadStartParams(LoadSave.PLAYER_HIT, 6);
        animations[JUMP] = loadStartParams(LoadSave.PLAYER_JUMP, 4);
        animations[FALLING] = animations[JUMP];
    }

    private BufferedImage[] loadStartParams(String filename, int frames) {
        BufferedImage sheet = LoadSave.GetSpriteAtlas(filename);
        BufferedImage[] arr = new BufferedImage[frames];
        for (int i = 0; i < frames; i++) {
            arr[i] = sheet.getSubimage(i * 48, 0, 48, 48);
        }
        return arr;
    }

    public BufferedImage getCurrentFrame() {
        return animations[playerAction][aniIndex];
    }

    public int getAniIndex() {
        return aniIndex;
    }
}