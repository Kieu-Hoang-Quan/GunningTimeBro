package utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io. InputStream;

public class LoadSave {

    // Player animations
    public static final String PLAYER_IDLE = "Player/Idle1.png";
    public static final String PLAYER_RUN = "Player/Run1.png";
    public static final String PLAYER_HIT = "Player/Attack1.png";
    public static final String PLAYER_JUMP = "Player/Jump1.png";
    public static final String PLAYER_HITRUN = "Player/Run_attack.png";
    public static final String PLAYER_LIGHTNING = "Player/SuperPower.png";

    @Deprecated
    public static BufferedImage GetSpriteAtlas(String fileName) {
        return loadSpriteAtlas(fileName);
    }

    public static BufferedImage loadSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        if (is == null) {
            System.err.println("[RESOURCE] File not found: " + fileName);
            return null;
        }

        try {
            img = ImageIO.read(is);
            System.out.println("[RESOURCE] Loaded: " + fileName);
        } catch (IOException e) {
            System.err.println("[RESOURCE] Error reading: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
}