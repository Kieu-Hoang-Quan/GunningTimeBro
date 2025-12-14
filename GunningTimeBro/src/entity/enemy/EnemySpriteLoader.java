package entity.enemy;

import utilz.LoadSave;
import java.awt.image.BufferedImage;
import static utilz.Constants.EnemyConstants.*;


public class EnemySpriteLoader {

    private static final String[] SPRITE_PATHS = {
            "Enemies/Idle.png",
            "Enemies/Walk.png",
            "Enemies/Attack.png",
            "Enemies/Hurt.png",
            "Enemies/Death.png"
    };

    /**
     * Load all enemy sprites
     */
    public static BufferedImage[][] loadEnemySprites() {
        BufferedImage[][] sprites = new BufferedImage[5][];

        for (int i = 0; i < SPRITE_PATHS.length; i++) {
            sprites[i] = loadSpriteSet(i, SPRITE_PATHS[i]);
        }

        return sprites;
    }

    private static BufferedImage[] loadSpriteSet(int state, String path) {
        int amount = GetSpriteAmount(CRABBY, state);
        BufferedImage atlas = LoadSave.GetSpriteAtlas(path);

        if (atlas == null) {
            System.err.println("[EnemySpriteLoader] Failed to load:  " + path);
            return createEmptyArray(amount);
        }

        BufferedImage[] frames = new BufferedImage[amount];
        int frameWidth = atlas.getWidth() / amount;
        int frameHeight = atlas.getHeight();

        for (int i = 0; i < amount; i++) {
            frames[i] = atlas.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
        }

        System.out.println("[EnemySpriteLoader] Loaded " + path + " (" + amount + " frames)");
        return frames;
    }

    private static BufferedImage[] createEmptyArray(int size) {
        BufferedImage[] arr = new BufferedImage[size];
        for (int i = 0; i < size; i++) {
            arr[i] = new BufferedImage(48, 48, BufferedImage. TYPE_INT_ARGB);
        }
        return arr;
    }
}