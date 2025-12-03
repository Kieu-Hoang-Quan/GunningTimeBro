package map;

import java.io.IOException;

public class LevelTileConfig {

    public static final int TILE_SIZE = 32;

    public static TileSet createTileSet() throws IOException {
   
        String filePath = "res/Map/tiles/Tiles.png";  

        return new TileSet(filePath, TILE_SIZE);
    }

  
    public static int[][] createLevelGrid() {
        int rows = 10;
        int cols = 360; // map dài 120 cột
        int[][] grid = new int[rows][cols];

        final int G = 73; // ground tile trong tileset
        final int P = 20; // platform tile (tuỳ bạn chọn ID nào đẹp)

        int groundRow = rows - 1;

        // 1. Trải ground full map
        for (int c = 0; c < cols; c++) {
            grid[groundRow][c] = G;
        }

        // 2. Tạo vài cái hố (giống khoảng trống trong video)
        clearRange(grid, groundRow, 10, 14); // hố 1
        clearRange(grid, groundRow, 30, 35); // hố 2
        clearRange(grid, groundRow, 55, 58); // hố 3
        clearRange(grid, groundRow, 80, 83); // hố 4
        clearRange(grid, groundRow, 90, 121); // hố 5

        // 3. Thêm platform ở trên cao
        int platRow1 = groundRow - 3; // cao vừa phải
        int platRow2 = groundRow - 5; // cao hơn\
        int platRow3 = groundRow - 7; // cao nhất

        fillRange(grid, platRow1, 11, 19, P);
        fillRange(grid, platRow1, 33, 40, P);
        fillRange(grid, platRow1, 57, 62, P);
        fillRange(grid, platRow1, 90, 90, P);
        fillRange(grid, platRow1, 92, 92, P);
        fillRange(grid, platRow1, 94, 94, P);

        fillRange(grid, platRow2, 22, 27, P);
        fillRange(grid, platRow2, 65, 75, P);
        fillRange(grid, platRow2, 96, 97, P);
        fillRange(grid, platRow2, 99, 100, P);
        fillRange(grid, platRow2, 102, 103, P);

        fillRange(grid, platRow3, 106, 109, P);
        fillRange(grid, platRow3, 112, 115, P);
        fillRange(grid, platRow3, 118, 121, P);


        // 4. Một số "bục" nhỏ kiểu bậc thang
        fillRange(grid, groundRow - 1, 5, 7, G);
        fillRange(grid, groundRow - 2, 6, 7, G);

        fillRange(grid, groundRow - 1, 47, 49, G);
        fillRange(grid, groundRow - 2, 48, 49, G);
        fillRange(grid, groundRow - 1, 54, 54, G);

        fillRange(grid, groundRow - 1, 87, 89, G);
        fillRange(grid, groundRow - 2, 88, 89, G);


        return grid;
    }

    // xoá ground tạo hố
    private static void clearRange(int[][] grid, int row, int fromCol, int toCol) {
        for (int c = fromCol; c <= toCol; c++) {
            grid[row][c] = 0;
        }
    }

    // vẽ đoạn platform/ground
    private static void fillRange(int[][] grid, int row, int fromCol, int toCol, int tileId) {
        for (int c = fromCol; c <= toCol; c++) {
            grid[row][c] = tileId;
        }
    }
}