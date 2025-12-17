package main;

import entity.enemy.EnemyManager;
import entity.player.Player;
import gamestates.GameStateRegistry;
import inputs.InputManager;
import ui.HealthBarUI;
import ui.ItemNotificationUI;
import world.World;

/**
 * Game Context - Chứa tất cả game services.
 *
 * SOLID: SRP - Centralized access point
 */
public class GameContext {

    public final World world;
    public Player player;  // Không final vì có thể thay đổi khi restart
    public EnemyManager enemyManager;  // Không final vì có thể thay đổi khi restart
    public final InputManager inputManager;
    public final GameStateRegistry stateRegistry;
    public HealthBarUI healthBarUI;
    public ItemNotificationUI itemNotificationUI;

    public GameContext(World world, Player player, EnemyManager enemyManager,
                       InputManager inputManager, GameStateRegistry stateRegistry,
                       HealthBarUI healthBarUI, ItemNotificationUI itemNotificationUI) {
        this.world = world;
        this.player = player;
        this.enemyManager = enemyManager;
        this.inputManager = inputManager;
        this.stateRegistry = stateRegistry;
        this.healthBarUI = healthBarUI;
        this.itemNotificationUI = itemNotificationUI;
    }

    /**
     * Update entities khi restart game
     */
    public void updateEntities(Player newPlayer, EnemyManager newEnemyManager) {
        this.player = newPlayer;
        this.enemyManager = newEnemyManager;

        // Tạo UI mới cho player mới
        this.healthBarUI = new HealthBarUI(newPlayer);
        this.itemNotificationUI = new ItemNotificationUI();
    }
}