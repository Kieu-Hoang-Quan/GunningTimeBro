package utilz;

import java. awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ItemSprites {

    private static Map<String, BufferedImage> spriteCache = new HashMap<>();

    public static class Paths {
        public static final String HEALTH_POTION = "Items/HealthPotion.png";
        public static final String LIGHTNING_POWER = "Items/LightningPower.png";
        public static final String DAMAGE_BOOST = "Items/DamageBoost.png";
    }

    public static BufferedImage getSprite(String path) {
        if (spriteCache. containsKey(path)) {
            return spriteCache.get(path);
        }

        BufferedImage sprite = utilz.ResourceLoader.loadSprite(path);

        if (sprite == null) {
            System.err.println("[ItemSprites] Failed to load:  " + path);
            sprite = createPlaceholder();
        } else {
            System.out.println("[ItemSprites] Loaded: " + path);
        }

        spriteCache.put(path, sprite);
        return sprite;
    }

    public static void preloadAll() {
        System.out.println("[ItemSprites] Preloading sprites...");

        getSprite(Paths.HEALTH_POTION);
        getSprite(Paths.LIGHTNING_POWER);
        getSprite(Paths.DAMAGE_BOOST);

        System.out.println("[ItemSprites] Preloaded " + spriteCache.size() + " sprites");
    }

    private static BufferedImage createPlaceholder() {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage. TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(java.awt.Color. MAGENTA);
        g.fillRect(0, 0, 32, 32);
        g.setColor(java.awt.Color. BLACK);
        g.drawString("? ", 12, 20);
        g.dispose();
        return img;
    }

    public static void clearCache() {
        spriteCache.clear();
    }
}