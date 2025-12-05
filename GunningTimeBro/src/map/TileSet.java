package map;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import java.io.InputStream;

public class TileSet {
    private final int tileSize;
    private BufferedImage sheet;
    private int cols;
    private int rows;

    public TileSet(String filePath, int tileSize) throws IOException {
        this. tileSize = tileSize;

        InputStream is = getClass().getResourceAsStream("/" + filePath);

        if (is == null) {
            throw new IOException("Resource not found: " + filePath);
        }

        try {
            this.sheet = ImageIO.read(is);
            this.cols = sheet.getWidth() / tileSize;
            this.rows = sheet.getHeight() / tileSize;

            System.out.println("[TileSet] Loaded: " + filePath);
            System.out.println("[TileSet] Size: " + cols + "x" + rows + " tiles");
        } finally {
            is.close();
        }
    }

    public void drawTile(Graphics2D g2, int tileId, int x, int y) {
        if (tileId <= 0) return;

        tileId--;

        int col = tileId % cols;
        int row = tileId / cols;

        int sx = col * tileSize;      // tileSize = 32
        int sy = row * tileSize;

        int destSize = Game.TILES_SIZE; // = 64, dùng cho world

        g2.drawImage(sheet,
                x, y, x + destSize, y + destSize,
                sx, sy, sx + tileSize, sy + tileSize,
                null);
    }

    public int getTileSize() {
        return tileSize;
    }
}
