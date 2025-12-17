package gamestates.systems;

import main.Game;
import world.World;
import entity.player.Player;
import entity.enemy.EnemyManager;

import java.awt.*;

/**
 * Coordinator cho Playing state.
 *
 * SOLID:
 * - SRP: Chỉ điều phối các systems
 * - DIP:  Nhận dependencies qua constructor, không gọi game.getXxx()
 */
public class GameplayCoordinator {

    private final PlayerSystem playerSystem;
    private final EnemySystem enemySystem;
    private final ItemSystem itemSystem;
    private final CameraSystem cameraSystem;

    // Giữ reference để render
    private final Player player;
    private final EnemyManager enemyManager;

    /**
     * Constructor - nhận trực tiếp dependencies thay vì Game
     *
     * @param player Player instance
     * @param enemyManager EnemyManager instance
     * @param world World instance
     */
    public GameplayCoordinator(Player player, EnemyManager enemyManager, World world) {
        this.player = player;
        this.enemyManager = enemyManager;

        this.playerSystem = new PlayerSystem(player);
        this.enemySystem = new EnemySystem(enemyManager);
        this.itemSystem = new ItemSystem(world. getItemManager(), player);
        this.cameraSystem = new CameraSystem(world.getTileMap());
    }

    public void tick() {
        playerSystem.update();
        enemySystem.update();
        itemSystem.update();
        cameraSystem.update(playerSystem.getPlayer());
    }

    public void render(Graphics2D g, World world) {
        int camX = cameraSystem.getCamX();

        world.getBgManager().render(g, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        world.getTileMap().render(g, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        world.getItemManager().render(g, camX);
        enemyManager.draw(g, camX);
        player.render(g, camX);

        if (cameraSystem.isSpectatorMode()) {
            cameraSystem.drawSpectatorUI(g, world. getTileMap());
        }
    }

    public CameraSystem getCameraSystem() { return cameraSystem; }
    public Player getPlayer() { return player; }
}