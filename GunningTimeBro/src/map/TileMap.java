package map;
import main.Game;
import map.TileSet;

import java.awt.*;

public class    TileMap {

    private final int[][] grid;
    private final TileSet tileSet;

    public TileMap(int[][] grid, TileSet tileSet) {
        this.grid = grid;
        this.tileSet = tileSet;
    }

    public int getMapWidthPixels() {
        // dùng TILE_SIZE trong world (64), không dùng 32 trong tileset
        return grid[0].length * Game.TILES_SIZE;
    }

    public void render(Graphics2D g2, int camX, int panelWidth, int panelHeight) {
        int rows = grid.length;
        int cols = grid[0].length;
        int tileSizeWorld = Game.TILES_SIZE;

        int firstCol = Math.max(0, camX / tileSizeWorld);
        int lastCol  = Math.min(cols - 1, (camX + panelWidth) / tileSizeWorld + 1);

        for (int r = 0; r < rows; r++) {
            for (int c = firstCol; c <= lastCol; c++) {
                int tileId = grid[r][c];
                if (tileId == 0) continue;

                int worldX = c * tileSizeWorld;
                int worldY = r * tileSizeWorld;

                int screenX = worldX - camX;
                int screenY = worldY;

                tileSet.drawTile(g2, tileId, screenX, screenY);
            }
        }
    }
}
