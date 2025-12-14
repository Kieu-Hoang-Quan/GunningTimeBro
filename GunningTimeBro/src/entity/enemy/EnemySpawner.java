package entity.enemy;

import main.Game;
import java.awt. geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Enemy spawner with YOUR exact spawn logic
 */
public class EnemySpawner {

    // ✅ Your exact spawn configuration
    private static final int MIN_PLATFORM_SIZE = 8;
    private static final int NO_SPAWN_LEFT = 300;
    private static final int NO_SPAWN_RIGHT_OFFSET = 20;

    /**
     * Spawn enemies - YOUR EXACT LOGIC
     */
    public static ArrayList<Enemy> spawnEnemies(int[][] lvlData) {
        System.out.println("[EnemySpawner] Starting enemy spawn...");

        ArrayList<Enemy> enemies = new ArrayList<>();

        int rows = lvlData.length;
        int cols = lvlData[0].length;

        // Tính toán giới hạn spawn
        int NO_SPAWN_RIGHT = (cols - NO_SPAWN_RIGHT_OFFSET) * Game.TILES_SIZE;

        System.out.println("[EnemySpawner] Map size: " + rows + "x" + cols);
        System.out.println("[EnemySpawner] Spawn zone: " + NO_SPAWN_LEFT + " to " + NO_SPAWN_RIGHT);

        for (int r = 0; r < rows; r++) {
            int c = 0;
            while (c < cols) {
                int tileId = lvlData[r][c];

                // --- LOGIC SPAWN TRÊN ĐẤT BẰNG (Tile 20) ---
                if (tileId == 20) {
                    int start = c;
                    while (c < cols && lvlData[r][c] == 20) c++;
                    int end = c - 1;
                    int width = end - start + 1;

                    if (width >= MIN_PLATFORM_SIZE) {
                        int mid = (start + end) / 2;
                        float spawnX = mid * Game.TILES_SIZE;

                        // ✔ Fix Spawn Y:  30 là chiều cao hitbox mới của Punk
                        float spawnY = (r * Game.TILES_SIZE) - (30 * Game.SCALE) - 1;

                        if (spawnX > NO_SPAWN_LEFT && spawnX < NO_SPAWN_RIGHT) {
                            if (!intersectsExisting(enemies, spawnX, spawnY)) {
                                enemies.add(new Crabby(spawnX, spawnY));
                                System.out.println("[EnemySpawner] Spawned on platform at (" + (int)spawnX + ", " + (int)spawnY + ")");
                            }
                        }
                    }
                }
                // --- LOGIC SPAWN TRÊN RÌA (Tile 73) ---
                else {
                    if (tileId == 73) {
                        boolean isGroundTop = (r == rows - 1 || lvlData[r + 1][c] == 0);
                        if (isGroundTop) {
                            int start = c;
                            while (c < cols && lvlData[r][c] == 73 &&
                                    (r == rows - 1 || lvlData[r + 1][c] == 0))
                                c++;
                            int end = c - 1;
                            int width = end - start + 1;

                            if (width >= MIN_PLATFORM_SIZE) {
                                int mid = (start + end) / 2;
                                float spawnX = mid * Game. TILES_SIZE;
                                float spawnY = (r * Game.TILES_SIZE) - (30 * Game.SCALE) - 1;

                                if (spawnX > NO_SPAWN_LEFT && spawnX < NO_SPAWN_RIGHT) {
                                    if (!intersectsExisting(enemies, spawnX, spawnY)) {
                                        enemies. add(new Crabby(spawnX, spawnY));
                                        System.out. println("[EnemySpawner] Spawned on ground at (" + (int)spawnX + ", " + (int)spawnY + ")");
                                    }
                                }
                            }
                            continue;
                        }
                    }
                    c++;
                }
            }
        }

        System.out.println("[EnemySpawner] Spawned " + enemies.size() + " enemies");
        return enemies;
    }

    // Helper check spawn chồng nhau
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