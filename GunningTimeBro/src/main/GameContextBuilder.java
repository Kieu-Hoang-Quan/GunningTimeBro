package main;

import entity.enemy.EnemyManager;
import entity.player.Player;
import gamestates.GameStateRegistry;
import inputs.InputManager;
import map.LevelTileConfig;
import ui. HealthBarUI;
import ui.ItemNotificationUI;
import world.World;

import java.io.IOException;

/**
 * Builder cho GameContext.
 *
 * SOLID:
 * - SRP:  Chỉ lo khởi tạo objects
 * - Không gọi game.getXxx() để tránh circular dependency
 */
public class GameContextBuilder {

    /**
     * Build GameContext với tất cả dependencies
     *
     * QUAN TRỌNG: Không gọi game.getPlayer() hay game.getEnemyManager()
     * vì context chưa được gán cho game
     */
    public GameContext build(Game game) throws IOException {
        // 1. Tạo InputManager (không phụ thuộc gì)
        InputManager inputManager = new InputManager();

        // 2. Tạo World (không phụ thuộc gì khác)
        World world = new World();

        // 3. Tạo Player
        float spawnX = 3 * Game.TILES_SIZE;
        float spawnY = 300;
        Player player = new Player(spawnX, spawnY,
                (int)(64 * Game.SCALE), (int)(40 * Game.SCALE));
        player.loadLvlData(LevelTileConfig.createLevelGrid());

        // 4. Tạo EnemyManager (cần Game cho một số thứ, nhưng không cần context)
        EnemyManager enemyManager = new EnemyManager(game);
        enemyManager.loadEnemies(LevelTileConfig.createLevelGrid());

        // 5. Kết nối Player với Enemies
        player.setEnemies(enemyManager. getEnemies());

        // 6. Tạo UI
        HealthBarUI healthBarUI = new HealthBarUI(player);
        ItemNotificationUI itemNotificationUI = new ItemNotificationUI();

        // 7. Tạo StateRegistry - truyền Player và EnemyManager trực tiếp
        GameStateRegistry stateRegistry = new GameStateRegistry(
                game, world, inputManager, player, enemyManager);
        stateRegistry.setState("menu");

        // 8. Return context
        return new GameContext(world, player, enemyManager, inputManager,
                stateRegistry, healthBarUI, itemNotificationUI);
    }
}