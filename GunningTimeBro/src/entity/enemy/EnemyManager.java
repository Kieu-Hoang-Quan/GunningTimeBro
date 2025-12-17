package entity.enemy;

import entity.enemy.systems.*;
import entity.player.Player;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Enemy Manager - Orchestrate systems.
 *
 * SOLID:  SRP - Chỉ orchestrate, delegate logic cho systems
 * NO LOOT: Đã xóa loot logic
 */
public class EnemyManager {

    private final Game game;
    private final BufferedImage[][] enemySprites;
    private ArrayList<Enemy> enemies;
    private int[][] lvlData;

    // Systems
    private final EnemyUpdateSystem updateSystem = new EnemyUpdateSystem();
    private final EnemyCombatSystem combatSystem = new EnemyCombatSystem();
    private final EnemyCleanupSystem cleanupSystem = new EnemyCleanupSystem();

    public EnemyManager(Game game) {
        this.game = game;
        this.enemies = new ArrayList<>();
        this.enemySprites = EnemySpriteLoader.loadEnemySprites();
    }

    public void loadEnemies(int[][] lvlData) {
        this.lvlData = lvlData;
        this.enemies = EnemySpawner.spawnEnemies(lvlData);
    }

    public void update() {
        Player player = game.getPlayer();
        if (player == null) return;

        updateSystem.update(enemies, lvlData, player);
        combatSystem.handleAttacks(enemies, player);
        cleanupSystem. cleanup(enemies);
    }

    public void draw(Graphics g, int xOff) {
        for (Enemy enemy :  enemies) {
            if (enemy.isActive()) {
                enemy.render(g, enemySprites, xOff);
            }
        }
    }

    public ArrayList<Enemy> getEnemies() { return enemies; }
    public void clear() { enemies.clear(); }
}