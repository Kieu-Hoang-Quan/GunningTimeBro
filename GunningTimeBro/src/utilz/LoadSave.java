package utilz;

import main.Game;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadSave {

    public static final String PLAYER_IDLE = "Player/Idle1.png";
    public static final String PLAYER_RUN = "Player/Run1.png";
    public static final String PLAYER_HIT = "Player/Biker_attack1.png";
    public static final String PLAYER_JUMP = "Player/Jump1.png";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
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

    private static BufferedImage createPlaceholder(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        // Transparent placeholder to prevent crashes
        return img;
    }

}