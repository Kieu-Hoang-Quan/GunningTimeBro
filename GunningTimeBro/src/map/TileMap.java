package map;

import java.awt.Graphics2D;

public class TileMap {

    private final int[][] grid;   // map: ID tile
    private final TileSet tileSet;

    public TileMap(int[][] grid, TileSet tileSet) {
        this.grid = grid;
        this.tileSet = tileSet;
    }

    public int getMapWidthPixels() {
        return grid[0].length * tileSet.getTileSize();
    }

    public void render(Graphics2D g2, int camX, int panelWidth, int panelHeight) {
        int rows = grid.length;
        int cols = grid[0].length;
        int tileSize = tileSet.getTileSize();

        int baseY = panelHeight - rows * tileSize;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int tileId = grid[r][c];
                if (tileId == 0) continue; // 0 = empty

                int worldX  = c * tileSize;
                int screenX = worldX - camX;
                int screenY = baseY + r * tileSize;

                tileSet.drawTile(g2, tileId, screenX, screenY);
            }
        }
    }
}