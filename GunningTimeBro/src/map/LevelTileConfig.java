package map;

import java.io.IOException;

public class LevelTileConfig {

    public static final int TILE_SIZE = 32;

    public static TileSet createTileSet() throws IOException {
        String filePath = "tiles/Tiles.png";
        return new TileSet(filePath, TILE_SIZE);
    }

    public static int[][] createLevelGrid() {
        // CHANGED: Increased rows to 14 to fill more screen vertical space
        int rows = 14;
        int cols = 100;
        int[][] grid = new int[rows][cols];

        final int G = 73;    // ground tile
        final int P = 20;    // purple platform tile

        int groundRow = rows - 1;

        // Trải ground full map
        for (int c = 0; c < cols; c++) {
            grid[groundRow][c] = G;
        }

        // Tạo hố
        clearRange(grid, groundRow, 10, 14); // hố 1
        clearRange(grid, groundRow, 30, 35); // hố 2
        clearRange(grid, groundRow, 55, 60); // hố 3
        clearRange(grid, groundRow, 80, 83); // hố 4

        // platform trên cao (adjusted relative to new groundRow)
        int platRow1 = groundRow - 3;
        int platRow2 = groundRow - 5;

        fillRange(grid, platRow1, 12, 18, P);
        fillRange(grid, platRow1, 36, 43, P);

        fillRange(grid, platRow2, 22, 27, P);
        fillRange(grid, platRow2, 65, 75, P);

        // bậc thang
        fillRange(grid, groundRow - 1, 5, 7, G);
        fillRange(grid, groundRow - 2, 6, 7, G);

        fillRange(grid, groundRow - 1, 47, 49, G);
        fillRange(grid, groundRow - 2, 48, 49, G);

        return grid;
    }

    private static void clearRange(int[][] grid, int row, int fromCol, int toCol) {
        for (int c = fromCol; c <= toCol; c++) {
            grid[row][c] = 0;
        }
    }

    private static void fillRange(int[][] grid, int row, int fromCol, int toCol, int tileId) {
        for (int c = fromCol; c <= toCol; c++) {
            grid[row][c] = tileId;
        }
    }
}