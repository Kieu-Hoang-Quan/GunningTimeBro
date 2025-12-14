package entity.enemy;

import main.Game;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

// ✅ Import Factory và Constants
import static utilz.Constants.EnemyConstants.CRABBY;

public class EnemySpawner {

    private static final int MIN_PLATFORM_SIZE = 8;
    private static final int NO_SPAWN_LEFT = 300;
    private static final int NO_SPAWN_RIGHT_OFFSET = 20;

    public static ArrayList<Enemy> spawnEnemies(int[][] lvlData) {
        System.out.println("[EnemySpawner] Starting enemy spawn...");

        ArrayList<Enemy> enemies = new ArrayList<>();

        int rows = lvlData.length;
        int cols = lvlData[0].length;
        int NO_SPAWN_RIGHT = (cols - NO_SPAWN_RIGHT_OFFSET) * Game.TILES_SIZE;

        System.out.println("[EnemySpawner] Map size: " + rows + "x" + cols);

        for (int r = 0; r < rows; r++) {
            int c = 0;
            while (c < cols) {
                int tileId = lvlData[r][c];

                // Nếu là đất bằng (20) hoặc rìa (73)
                if (tileId == 20 || tileId == 73) {
                    boolean isValidSpawn = false;
                    int start = c;
                    int width = 0;

                    // Logic kiểm tra độ rộng sàn (giữ nguyên logic gốc của bạn)
                    if (tileId == 20) {
                        while (c < cols && lvlData[r][c] == 20) c++;
                        width = (c - 1) - start + 1;
                        isValidSpawn = true;
                    } else { // tileId == 73
                        boolean isGroundTop = (r == rows - 1 || lvlData[r + 1][c] == 0);
                        if (isGroundTop) {
                            while (c < cols && lvlData[r][c] == 73 && (r == rows - 1 || lvlData[r + 1][c] == 0)) c++;
                            width = (c - 1) - start + 1;
                            isValidSpawn = true;
                        } else {
                            c++;
                        }
                    }

                    if (isValidSpawn && width >= MIN_PLATFORM_SIZE) {
                        int end = c - 1;
                        int mid = (start + end) / 2;
                        float spawnX = mid * Game.TILES_SIZE;
                        float spawnY = (r * Game.TILES_SIZE) - (30 * Game.SCALE) - 1;

                        if (spawnX > NO_SPAWN_LEFT && spawnX < NO_SPAWN_RIGHT) {
                            if (!intersectsExisting(enemies, spawnX, spawnY)) {

                                // ✅ SỬ DỤNG FACTORY ĐỂ TẠO QUÁI
                                // Hiện tại mặc định là CRABBY, sau này có thể random hoặc check map
                                Enemy newEnemy = EnemyFactory.createEnemy(CRABBY, spawnX, spawnY);

                                if (newEnemy != null) {
                                    enemies.add(newEnemy);
                                    System.out.println("[EnemySpawner] Spawned enemy at (" + (int)spawnX + ", " + (int)spawnY + ")");
                                }
                            }
                        }
                    }
                    if (tileId == 20) continue; // Đã tăng c trong vòng while
                }
                c++;
            }
        }

        System.out.println("[EnemySpawner] Spawned " + enemies.size() + " enemies");
        return enemies;
    }

    private static boolean intersectsExisting(ArrayList<Enemy> enemies, float x, float y) {
        Rectangle2D.Float hb = new Rectangle2D.Float(x, y, 50, 50);
        for (Enemy e : enemies) {
            if (hb.intersects(e.getHitbox())) {
                return true;
            }
        }
        return false;
    }
}