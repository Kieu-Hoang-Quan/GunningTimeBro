package entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import main.Game;
import utilz.LoadSave;
import utilz.Constants.EnemyConstants;

public class EnemyManager {

    private Game game;
    private ArrayList<Enemies> enemies = new ArrayList<>();
    private BufferedImage[][] enemySprites;
    private int[][] lvlData;

    // ========= CẤU HÌNH AN TOÀN SPAWN ==========
    private final int tile = 64;
    private final int MIN_PLATFORM_SIZE = 8;      // Không spawn trên platform nhỏ
    private final int NO_SPAWN_LEFT = 300;        // Không spawn đầu map
    private final int NO_SPAWN_RIGHT_OFFSET = 20; // Không spawn cuối map

    public EnemyManager(Game game) {
        this.game = game;
        loadEnemySprites();
    }

    // ============================================================
    //                     SPAWN ENEMIES
    // ============================================================
    public void loadEnemies(int[][] lvlData) {

        this.lvlData = lvlData;
        enemies.clear();

        int rows = lvlData.length;
        int cols = lvlData[0].length;

        int NO_SPAWN_RIGHT = (cols - NO_SPAWN_RIGHT_OFFSET) * tile;

        for (int r = 0; r < rows; r++) {

            int c = 0;

            while (c < cols) {
                int tileId = lvlData[r][c];

                // ============================================
                //                SPAWN TRÊN PLATFORM
                // ============================================
                if (tileId == 20) { // platform tile

                    int start = c;

                    while (c < cols && lvlData[r][c] == 20) c++;
                    int end = c - 1;

                    int width = end - start + 1;

                    // Chỉ spawn platform đủ lớn
                    if (width >= MIN_PLATFORM_SIZE) {

                        int mid = (start + end) / 2;
                        float spawnX = mid * tile;
                        float spawnY = (r * tile) - 64 + 5;

                        if (spawnX > NO_SPAWN_LEFT && spawnX < NO_SPAWN_RIGHT) {

                            Rectangle2D.Float hb = new Rectangle2D.Float(spawnX, spawnY, 134, 59);

                            if (!intersectsExisting(hb)) {
                                enemies.add(new Crabby(spawnX, spawnY));
                            }
                        }
                    }

                } else {

                    // ============================================
                    //                 SPAWN TRÊN GROUND
                    // ============================================
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

                                float spawnX = mid * tile;
                                float spawnY = (r * tile) - 64 + 5;

                                if (spawnX > NO_SPAWN_LEFT && spawnX < NO_SPAWN_RIGHT) {

                                    Rectangle2D.Float hb = new Rectangle2D.Float(spawnX, spawnY, 134, 59);

                                    if (!intersectsExisting(hb)) {
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

        System.out.println("Spawned enemies: " + enemies.size());
    }

    // ============================================================
    private boolean intersectsExisting(Rectangle2D.Float hb) {
        for (Enemies e : enemies) {
            if (hb.intersects(e.getHitbox())) return true;
        }
        return false;
    }

    // ============================================================
    //                 LOAD SPRITES
    // ============================================================
    private void loadEnemySprites() {
        enemySprites = new BufferedImage[5][];

        loadSpriteSet(0, "Enemies/Idle.png");
        loadSpriteSet(1, "Enemies/Walk.png");
        loadSpriteSet(2, "Enemies/Attack.png");
        loadSpriteSet(3, "Enemies/Hurt.png");
        loadSpriteSet(4, "Enemies/Death.png");
    }

    private void loadSpriteSet(int state, String path) {
        int amount = EnemyConstants.GetSpriteAmount(0, state);
        BufferedImage atlas = LoadSave.GetSpriteAtlas(path);

        if (atlas == null)
            atlas = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);

        enemySprites[state] = new BufferedImage[amount];

        int fw = atlas.getWidth() / amount;
        int fh = atlas.getHeight();

        for (int i = 0; i < amount; i++) {
            enemySprites[state][i] = atlas.getSubimage(i * fw, 0, fw, fh);
        }
    }

    // ============================================================
    //                     UPDATE & DRAW
    // ============================================================
    public void update() {
        for (Enemies e : enemies) {
            if (e.isAlive()) {

                e.update(lvlData);

                if (e.getEnemyState() == 2 &&
                        !e.attackChecked &&
                        e.isAttackFrame()) {

                    game.getPlayer().receiveDamageFromEnemy(
                            e.getAttackBox(), e.attackDamage
                    );
                    e.attackChecked = true;
                }
            }
        }
    }

    public void draw(Graphics g, int xOff) {
        for (Enemies e : enemies) {
            if (e.isAlive()) {

                int state = e.getEnemyState();
                int frame = e.getAniIndex();

                int W = 144, H = 64;

                float hbW = e.getHitbox().width;
                float hbH = e.getHitbox().height;

                float xOffDraw = (W - hbW) / 2;
                float yOffDraw = (H - hbH);

                int x = (int) (e.getHitbox().x - xOffDraw - xOff);
                int y = (int) (e.getHitbox().y - yOffDraw);

                int flip = e.isFlip() ? -1 : 1;

                g.drawImage(
                        enemySprites[state][frame],
                        x + (flip == -1 ? W : 0),
                        y,
                        W * flip, H,
                        null
                );
            }
        }
    }

    public ArrayList<Enemies> getEnemies() {
        return enemies;
    }
}





