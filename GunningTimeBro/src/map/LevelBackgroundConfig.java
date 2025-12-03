package map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LevelBackgroundConfig {

    public static void setupFactoryBackground(BgManager bgManager) throws IOException {
        bgManager.clear();


        BufferedImage bg0 = ImageIO.read(new File("res/Map/background/bg0.png")); // màu nền xám
        BufferedImage bg1 = ImageIO.read(new File("res/Map/background/bg1.png")); // mây
        BufferedImage bg2 = ImageIO.read(new File("res/Map/background/bg2.png")); // city xa
        BufferedImage bg3 = ImageIO.read(new File("res/Map/background/bg3.png")); // city gần
        BufferedImage bg4 = ImageIO.read(new File("res/Map/background/bg4.png")); // tank to phía trước

        bgManager.addLayer(new BgLayer(bg0, 0.00f, 0));  
        bgManager.addLayer(new BgLayer(bg1, 0.04f, 0));
        bgManager.addLayer(new BgLayer(bg2, 0.08f, 0));
        bgManager.addLayer(new BgLayer(bg3, 0.16f, 0));
        bgManager.addLayer(new BgLayer(bg4, 0.28f, 0));
    }
}