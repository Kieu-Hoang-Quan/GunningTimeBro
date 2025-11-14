package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16*16  tile. default
    final int scale = 3; // 16*3 scale

    public final int tileSize = originalTileSize * scale; // 48*48 tile // public cuz we use it in Player Class
    public final int maxScreenCol = 20; // 4:3 window
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;  //48*20 = 960 pixels
    public final int screenHeight = tileSize * maxScreenRow;  //48*12 = 576

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
    }
}