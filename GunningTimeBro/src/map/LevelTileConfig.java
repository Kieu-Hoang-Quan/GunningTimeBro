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
        int rows = 10;
        int cols = 360;
        int[][] grid = new int[rows][cols];

        final int G = 73;    // ground tile
        final int P = 20;    // purple platform tile

        int groundRow = rows - 1;

        // Trải ground full map
        for (int c = 0; c < cols; c++) {
            grid[groundRow][c] = G;
        }

        // 2. Tạo hố (giống khoảng trống trong video)
        clearRange(grid, groundRow, 10, 14); // hố 1
        clearRange(grid, groundRow, 30, 35); // hố 2
        clearRange(grid, groundRow, 55, 58); // hố 3
        clearRange(grid, groundRow, 80, 83); // hố 4
        clearRange(grid, groundRow, 90, 115); // hố 5
        clearRange(grid, groundRow, 118, 119); // hố 6
        clearRange(grid, groundRow, 165, 185); // hố 7

        // 3. Thêm platform ở trên cao

        int platRow0 = groundRow - 1; // thấp nhất
        int platRow1 = groundRow - 3; // cao vừa phải
        int platRow2 = groundRow - 5; // cao hơn
        int platRow3 = groundRow - 7; // cao nhất

        fillRange(grid, platRow0, 11, 13, P);
        
        fillRange(grid, platRow1, 11, 19, P);
        fillRange(grid, platRow1, 33, 40, P);
        fillRange(grid, platRow1, 57, 62, P);
        fillRange(grid, platRow1, 90, 90, P);
        fillRange(grid, platRow1, 92, 92, P);
        fillRange(grid, platRow1, 94, 94, P);
        fillRange(grid, platRow1, 134, 155, P);

        fillRange(grid, platRow2, 21, 27, P);
        fillRange(grid, platRow2, 65, 75, P);
        fillRange(grid, platRow2, 96, 97, P);
        fillRange(grid, platRow2, 99, 100, P);
        fillRange(grid, platRow2, 102, 103, P);
        fillRange(grid, platRow2, 165, 185, P);

        fillRange(grid, platRow3, 105, 107, P);
        fillRange(grid, platRow3, 109, 111, P);
        fillRange(grid, platRow3, 113, 115, P);
        fillRange(grid, platRow3, 134, 155, P);
        
        // 4. Một số "bục" nhỏ kiểu bậc thang
        fillRange(grid, groundRow - 1, 5, 7, G);
        fillRange(grid, groundRow - 2, 6, 7, G);

        fillRange(grid, groundRow - 1, 47, 49, G);
        fillRange(grid, groundRow - 2, 48, 49, G);
        fillRange(grid, groundRow - 1, 54, 54, G);

        fillRange(grid, groundRow - 1, 87, 89, G);
        fillRange(grid, groundRow - 2, 88, 89, G);

        fillRange(grid, groundRow - 1, 128, 132, G);
        fillRange(grid, groundRow - 2, 129, 131, G);

        fillRange(grid, groundRow - 3, 156, 156, G);
        fillRange(grid, groundRow - 4, 156, 156, G);
        fillRange(grid, groundRow - 5, 156, 156, G);
        fillRange(grid, groundRow - 6, 156, 156, G);
        fillRange(grid, groundRow - 7, 156, 156, G);

        fillRange(grid, groundRow - 1, 160, 164, G);
        fillRange(grid, groundRow - 2, 161, 164, G);
        fillRange(grid, groundRow - 3, 162, 164, G);
        fillRange(grid, groundRow - 4, 163, 164, G);
        fillRange(grid, groundRow - 5, 164, 164, G);

        fillRange(grid, groundRow - 5, 186, 186, G);
        fillRange(grid, groundRow - 4, 186, 187, G);
        fillRange(grid, groundRow - 3, 186, 188, G);
        fillRange(grid, groundRow - 2, 186, 189, G);
        fillRange(grid, groundRow - 1, 186, 190, G);

        fillRange(grid, groundRow - 3, 200, 200, G);
        fillRange(grid, groundRow - 4, 200, 200, G);
        fillRange(grid, groundRow - 5, 200, 200, G);
        fillRange(grid, groundRow - 6, 200, 200, G);
        fillRange(grid, groundRow - 7, 200, 200, G);
        fillRange(grid, groundRow - 8, 200, 200, G);
        fillRange(grid, groundRow - 9, 200, 200, G);
        
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
