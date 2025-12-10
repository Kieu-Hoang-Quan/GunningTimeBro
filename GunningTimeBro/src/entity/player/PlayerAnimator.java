package entity.player;

import utilz.LoadSave;
import java.awt.image.BufferedImage;
import static utilz.Constants.PlayerConstants.*;

public class PlayerAnimator {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int playerAction = IDLE;

    public PlayerAnimator() {
        loadAnimations();
    }

    public void update(int currentAction) {
        // Nếu action thay đổi, reset animation
        if (playerAction != currentAction) {
            playerAction = currentAction;
            aniTick = 0;
            aniIndex = 0;
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

    private void loadAnimations() {
        animations = new BufferedImage[9][6];

        // Helper func to load subimages cleanly (Optional refactor)
        animations[IDLE] = loadStartParams(LoadSave.PLAYER_IDLE, 4);
        animations[RUNNING] = loadStartParams(LoadSave.PLAYER_RUN, 6);
        animations[HIT] = loadStartParams(LoadSave.PLAYER_HIT, 6);
        animations[JUMP] = loadStartParams(LoadSave.PLAYER_JUMP, 4);
        animations[FALLING] = animations[JUMP]; // Reuse jump for falling
    }

    private BufferedImage[] loadStartParams(String filename, int frames) {
        BufferedImage sheet = LoadSave.GetSpriteAtlas(filename);
        BufferedImage[] arr = new BufferedImage[frames];
        for (int i = 0; i < frames; i++) {
            // Assuming sprite size is 48x48 as per your original code
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