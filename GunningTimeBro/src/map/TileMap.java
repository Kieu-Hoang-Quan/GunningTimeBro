package map;

import java.awt.Graphics2D;

public class TileMap {

    private final int[][] grid;   
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

        int baseY = 0;
    
        int firstCol = Math.max(0, camX / tileSize);
        int lastCol  = Math.min(cols - 1, (camX + panelWidth) / tileSize + 1);

        for (int r = 0; r < rows; r++) {
            for (int c = firstCol; c <= lastCol; c++) {  
                int tileId = grid[r][c];
                if (tileId == 0) continue;
        
                int worldX  = c * tileSize;
                int screenX = worldX - camX;
                int screenY = baseY + r * tileSize;
        
                tileSet.drawTile(g2, tileId, screenX, screenY);
            }
        }
    }
}