package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io. InputStream;
import javax.imageio.ImageIO;
public class LoadSave {

    public static final String PLAYER_IDLE = "Player/Idle1.png";
    public static final String PLAYER_RUN = "Player/Run1.png";
    public static final String PLAYER_HIT = "Player/Biker_attack1.png";
    public static final String PLAYER_JUMP = "Player/Jump1.png";


    @Deprecated
    public static BufferedImage GetSpriteAtlas(String fileName) {
        return ResourceLoader.loadSprite(fileName);
    }

    private static BufferedImage createPlaceholder(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        return img;
    }
}