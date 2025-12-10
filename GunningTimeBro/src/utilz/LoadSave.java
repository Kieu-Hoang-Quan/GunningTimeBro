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
    public static final String ENEMIES_ATTACK = "Enemies/Attack.png";
    public static final String ENEMIES_DEATH = "Enemies/Death.png";
    public static final String ENEMIES_HURT = "Enemies/Hurt.png";
    public static final String ENEMIES_IDLE = "Enemies/Idle.png";
    public static final String ENEMIES_WALK = "Enemies/Walk.png";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            if (is == null) {
                System.out.println("⚠ WARNING: Missing file → " + fileName);
                return createPlaceholder((int) (48 * Game.SCALE), (int) (48 * Game.SCALE));
            }

            img = ImageIO.read(is);

        } catch (IOException e) {
            System.out.println("⚠ ERROR loading image → " + fileName);
            return createPlaceholder((int) (48 * Game.SCALE), (int) (48 * Game.SCALE));

        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {}
        }
        return img;
    }


    private static BufferedImage createPlaceholder(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        // Transparent placeholder to prevent crashes
        return img;
    }

}