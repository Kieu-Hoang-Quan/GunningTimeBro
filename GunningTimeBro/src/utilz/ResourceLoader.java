package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java. util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ResourceLoader {

    // ════════════════════════════════════════════════════════
    //  SPRITE PATH CONSTANTS
    // ════════════════════════════════════════════════════════

    public static class PlayerSprites {
        public static final String IDLE = "Player/Idle1.png";
        public static final String RUN = "Player/Run1.png";
        public static final String ATTACK = "Player/Attack1.png";
        public static final String JUMP = "Player/Jump1.png";
        public static final String ATTACK_RUN = "Player/Attack1.png";  // ✅ Use Attack1 as fallback
        public static final String SUPERPOWER = "Player/Attack1.png";  // ✅ Use Attack1 as fallback
    }

    public static class EnemySprites {
        public static final String IDLE = "Enemies/Idle. png";
        public static final String WALK = "Enemies/Walk.png";
        public static final String ATTACK = "Enemies/Attack. png";
        public static final String HURT = "Enemies/Hurt.png";
        public static final String DEATH = "Enemies/Death.png";
    }

    public static class WorldSprites {
        // Add world sprites here
    }

    // ════════════════════════════════════════════════════════
    //  CACHING SYSTEM
    // ════════════════════════════════════════════════════════

    private static final Map<String, BufferedImage> cache = new HashMap<>();
    private static boolean enableCache = true;

    public static BufferedImage loadSprite(String fileName) {
        // Check cache first
        if (enableCache && cache.containsKey(fileName)) {
            return cache. get(fileName);
        }

        BufferedImage img = loadImageFromResources(fileName);

        // ✅ Cache successful loads
        if (img != null && enableCache) {
            cache. put(fileName, img);
        }

        return img;
    }

    private static BufferedImage loadImageFromResources(String fileName) {
        InputStream is = null;
        try {
            is = ResourceLoader.class.getResourceAsStream("/" + fileName);

            if (is == null) {
                System.err.println("[RESOURCE] File not found: " + fileName);
                return createErrorPlaceholder();
            }

            BufferedImage img = ImageIO.read(is);
            System.out.println("[RESOURCE] Loaded: " + fileName);
            return img;

        } catch (IOException e) {
            System.err.println("[RESOURCE] Error loading:  " + fileName);
            e.printStackTrace();
            return createErrorPlaceholder();

        } finally {
            closeStream(is);
        }
    }

    private static void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                System.err.println("[RESOURCE] Error closing stream");
            }
        }
    }

    private static BufferedImage createErrorPlaceholder() {
        BufferedImage img = new BufferedImage(48, 48, BufferedImage. TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(java.awt.Color. MAGENTA);
        g.fillRect(0, 0, 48, 48);
        g.setColor(java.awt.Color. BLACK);
        g.drawString("ERR", 10, 25);
        g.dispose();
        return img;
    }


    public static void clearCache() {
        cache.clear();
        System.out.println("[RESOURCE] Cache cleared");
    }

    public static void setCacheEnabled(boolean enabled) {
        enableCache = enabled;
    }

    public static int getCacheSize() {
        return cache.size();
    }

    @Deprecated
    public static BufferedImage GetSpriteAtlas(String fileName) {
        return loadSprite(fileName);
    }
}