package map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LevelBackgroundConfig {

    public static void setupFactoryBackground(BgManager bgManager) throws IOException {
        bgManager.clear();


        BufferedImage bg0 = ImageIO.read(new File("bg0 copy.png")); // màu nền xám
        BufferedImage bg1 = ImageIO.read(new File("bg1 copy.png")); // mây
        BufferedImage bg2 = ImageIO.read(new File("bg2 copy.png")); // city xa
        BufferedImage bg3 = ImageIO.read(new File("bg3 copy.png")); // city gần
        BufferedImage bg4 = ImageIO.read(new File("bg4 copy.png")); // tank to phía trước

        bgManager.addLayer(new BgLayer(bg0, 0.00f, 0));  
        bgManager.addLayer(new BgLayer(bg1, 0.04f, 0));
        bgManager.addLayer(new BgLayer(bg2, 0.08f, 0));
        bgManager.addLayer(new BgLayer(bg3, 0.16f, 0));
        bgManager.addLayer(new BgLayer(bg4, 0.28f, 0));
    }
}