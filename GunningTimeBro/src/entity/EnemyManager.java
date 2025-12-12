package entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entity.player.Player;
import main.Game;
import utilz.LoadSave;
import utilz.Constants.EnemyConstants;

// ✔✔✔ IMPORT BẮT BUỘC: Để EnemyManager hiểu Enemies và Crabby là gì
import entity.enemy.Enemies;
import entity.enemy.Crabby;

public class EnemyManager {

    private Game game;
    private ArrayList<Enemies> enemies = new ArrayList<>();
    private BufferedImage[][] enemySprites;
    private int[][] lvlData;

    private final int tile = 64; // Kích thước tile gốc chưa scale (hoặc đã tính toán)
    // Lưu ý: Nếu game bạn dùng TILES_SIZE public static thì dùng Game.TILES_SIZE sẽ chuẩn hơn

    private final int MIN_PLATFORM_SIZE = 8;
    private final int NO_SPAWN_LEFT = 300;
    private final int NO_SPAWN_RIGHT_OFFSET = 20;

    public EnemyManager(Game game) {
        this.game = game;
        loadEnemySprites();
    }

    public void loadEnemies(int[][] lvlData) {
        this.lvlData = lvlData;
        enemies.clear();

        int rows = lvlData.length;
        int cols = lvlData[0].length;

        // Tính toán giới hạn spawn
        int NO_SPAWN_RIGHT = (cols - NO_SPAWN_RIGHT_OFFSET) * Game.TILES_SIZE;

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

                        // ✔ Fix Spawn Y: 30 là chiều cao hitbox mới của Punk
                        float spawnY = (r * Game.TILES_SIZE) - (30 * Game.SCALE) - 1;

                        if (spawnX > NO_SPAWN_LEFT && spawnX < NO_SPAWN_RIGHT) {
                            if (!intersectsExisting(spawnX, spawnY)) {
                                enemies.add(new Crabby(spawnX, spawnY));
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
                                float spawnX = mid * Game.TILES_SIZE;
                                float spawnY = (r * Game.TILES_SIZE) - (30 * Game.SCALE) - 1;

                                if (spawnX > NO_SPAWN_LEFT && spawnX < NO_SPAWN_RIGHT) {
                                    if (!intersectsExisting(spawnX, spawnY)) {
                                        enemies.add(new Crabby(spawnX, spawnY));
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
    }

    // Helper check spawn chồng nhau
    private boolean intersectsExisting(float x, float y) {
        // Tạo hitbox ảo để check
        Rectangle2D.Float hb = new Rectangle2D.Float(x, y, 50, 50);
        for (Enemies e : enemies) {
            if (hb.intersects(e.getHitbox())) return true;
        }
        return false;
    }

    private void loadEnemySprites() {
        enemySprites = new BufferedImage[5][];

        loadSpriteSet(0, "Enemies/Idle.png");
        loadSpriteSet(1, "Enemies/Walk.png"); // Hoặc Run.png tùy tên file của bạn
        loadSpriteSet(2, "Enemies/Attack.png");
        loadSpriteSet(3, "Enemies/Hurt.png"); // Hoặc Hit.png
        loadSpriteSet(4, "Enemies/Death.png"); // Hoặc Dead.png
    }

    private void loadSpriteSet(int state, String path) {
        // Lấy số lượng frame chuẩn từ Constants (đã fix 4, 6, 6...)
        int amount = EnemyConstants.GetSpriteAmount(EnemyConstants.CRABBY, state);
        BufferedImage atlas = LoadSave.GetSpriteAtlas(path);

        if (atlas == null) {
            System.out.println("Lỗi load ảnh: " + path);
            return;
        }

        enemySprites[state] = new BufferedImage[amount];

        // Cắt ảnh tự động dựa trên chiều rộng ảnh / số lượng frame
        int fw = atlas.getWidth() / amount;
        int fh = atlas.getHeight();

        for (int i = 0; i < amount; i++) {
            enemySprites[state][i] = atlas.getSubimage(i * fw, 0, fw, fh);
        }
    }

    public void update() {
        Player player = game.getPlayer();
        if (player == null) return;

        for (Enemies e : enemies) {
            if (e.isActive()) {
                // Enemy tự gọi các component (Physics, AI, Animator) bên trong hàm update này
                e.update(lvlData, player);

                // Logic va chạm
                if (e.getEnemyState() == EnemyConstants.ATTACK &&
                        !e.attackChecked &&
                        e.isAttackFrame()) {

                    game.PlayerHit(e.getAttackBox(), e.attackDamage);
                    e.attackChecked = true;
                }
            }
        }
        enemies.removeIf(e -> !e.isActive());
    }

    public void draw(Graphics g, int xOff) {
        for (Enemies e : enemies) {
            // Gọi hàm render của Enemy -> nó sẽ tự gọi EnemyRender
            e.render(g, enemySprites, xOff);
        }
    }

    public ArrayList<Enemies> getEnemies() {
        return enemies;
    }
}