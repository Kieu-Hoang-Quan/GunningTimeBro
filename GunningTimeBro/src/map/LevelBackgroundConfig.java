package map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LevelBackgroundConfig {

    public static void setupFactoryBackground(BgManager bgManager) throws IOException {
        bgManager.clear();

        BufferedImage bg0 = ImageIO.read(LevelBackgroundConfig.class.getResourceAsStream("/background/bg0.png"));
        BufferedImage bg1 = ImageIO.read(LevelBackgroundConfig.class.getResourceAsStream("/background/bg1.png"));
        BufferedImage bg2 = ImageIO.read(LevelBackgroundConfig.class.getResourceAsStream("/background/bg2.png"));
        BufferedImage bg3 = ImageIO.read(LevelBackgroundConfig.class.getResourceAsStream("/background/bg3.png"));
        BufferedImage bg4 = ImageIO.read(LevelBackgroundConfig.class.getResourceAsStream("/background/bg4.png"));

        bgManager.addLayer(new BgLayer(bg0, 0.00f, 0));
        bgManager.addLayer(new BgLayer(bg1, 0.04f, 0));
        bgManager.addLayer(new BgLayer(bg2, 0.08f, 0));
        bgManager.addLayer(new BgLayer(bg3, 0.16f, 0));
        bgManager.addLayer(new BgLayer(bg4, 0.28f, 0));
    }
}