package map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;

public class TileSet {
    private final int tileSize;
    private BufferedImage sheet;
    private int cols;
    private int rows;

    public TileSet(String filePath, int tileSize) throws IOException {
        this.tileSize = tileSize;

        File f = new File(filePath);
        System.out.println("[TileSet] Path:   " + f.getAbsolutePath());
        System.out.println("[TileSet] Exists? " + f.exists());

        this.sheet = ImageIO.read(f);

        this.cols = sheet.getWidth() / tileSize;
        this.rows = sheet.getHeight() / tileSize;
    }

    public void drawTile(Graphics2D g2, int tileId, int x, int y) {
        if (tileId <= 0) return;

        tileId--; 

        int col = tileId % cols;
        int row = tileId / cols;

        int sx = col * tileSize;
        int sy = row * tileSize;

        g2.drawImage(sheet,
                x, y, x + tileSize, y + tileSize,
                sx, sy, sx + tileSize, sy + tileSize,
                null);
    }

    public int getTileSize() {
        return tileSize;
    }
}
