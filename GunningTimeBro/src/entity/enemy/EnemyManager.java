package entity.enemy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import entity.items.LightningPowerItem;
import entity.player.Player;
import main.Game;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    private Game game;
    private ArrayList<Enemy> enemies;
    private BufferedImage[][] enemySprites;
    private int[][] lvlData;
    private Random random;

    public EnemyManager(Game game) {
        this.game = game;
        this.enemies = new ArrayList<>();
        this.random = new Random();
        this.enemySprites = EnemySpriteLoader.loadEnemySprites();
    }

    public void loadEnemies(int[][] lvlData) {
        this.lvlData = lvlData;

        // ✅ Uses YOUR exact spawn logic from EnemySpawner
        this.enemies = EnemySpawner.spawnEnemies(lvlData);
    }

    public void update() {
        Player player = game.getPlayer();
        if (player == null) return;

        for (Enemy enemy : enemies) {
            if (! enemy.isActive()) continue;

            boolean wasAlive = enemy.getHealthComponent().isAlive();

            enemy.update(lvlData, player);

            handleEnemyAttack(enemy, player);

            if (wasAlive && ! enemy.getHealthComponent().isAlive()) {
                handleEnemyDeath(enemy);
            }
        }

        enemies.removeIf(e -> !e.isActive());
    }

    private void handleEnemyAttack(Enemy enemy, Player player) {
        if (enemy.getEnemyState() != ATTACK) return;
        if (enemy.attackChecked) return;
        if (! enemy.isAttackFrame()) return;

        if (enemy.getAttackBox().intersects(player.getHitbox())) {
            player.getHealthComponent().takeDamage(enemy.attackDamage, enemy);
        }

        enemy.attackChecked = true;
    }

    private void handleEnemyDeath(Enemy enemy) {
        float dropChance = random.nextFloat();

        float x = enemy.getHitbox().x;
        float y = enemy.getHitbox().y;

        if (dropChance < 0.4f) {
            game.getWorld().getItemManager().spawnItem(
                    x, y,
                    new LightningPowerItem(),
                    utilz.ItemSprites.getSprite(utilz.ItemSprites. Paths.LIGHTNING_POWER)
            );
        }
    }

    public void draw(Graphics g, int xOff) {
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                enemy.render(g, enemySprites, xOff);
            }
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void clear() {
        enemies.clear();
    }
}