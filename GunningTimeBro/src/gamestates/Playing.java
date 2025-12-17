package gamestates;

import main.Game;
import world.World;
import entity.player.Player;
import entity.enemy.EnemyManager;
import gamestates.systems.GameplayCoordinator;
import inputs.InputManager;
import inputs.InputManager.Action;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Playing State - Refactored.
 *
 * SOLID:
 * - SRP:  Chỉ điều phối gameplay
 * - DIP:  Nhận dependencies qua constructor
 */
public class Playing extends State {

    private final World world;
    private final Player player;
    private final GameplayCoordinator coordinator;
    private final InputManager inputManager;

    private boolean attackKeyPressed = false;
    private boolean lightningKeyPressed = false;
    private boolean playerFell = false;

    /**
     * Constructor - nhận tất cả dependencies trực tiếp
     */
    public Playing(Game game, World world, Player player,
                   EnemyManager enemyManager, InputManager inputManager) {
        super(game);
        this.world = world;
        this.player = player;
        this.inputManager = inputManager;

        // Tạo coordinator với dependencies trực tiếp
        this.coordinator = new GameplayCoordinator(player, enemyManager, world);
    }

    @Override
    public void update() {
        handlePlayerInput();
        coordinator.tick();
        checkPlayerFall();
    }

    private void handlePlayerInput() {
        player.setLeft(inputManager.isActionActive(Action. MOVE_LEFT));
        player.setRight(inputManager.isActionActive(Action.MOVE_RIGHT));
        player.setJump(inputManager.isActionActive(Action.JUMP));

        boolean attackNow = inputManager.isActionActive(Action.ATTACK);
        if (attackNow && !attackKeyPressed) {
            player.setAttacking(true);
        }
        attackKeyPressed = attackNow;

        boolean lightningNow = inputManager.isActionActive(Action. LIGHTNING);
        if (lightningNow && !lightningKeyPressed) {
            player.useLightningPower();
        }
        lightningKeyPressed = lightningNow;
    }

    private void checkPlayerFall() {
        if (player.getHitbox().y > Game.GAME_HEIGHT + 100 && !playerFell) {
            player.getHealthComponent().takeDamage(999, null);
            playerFell = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        coordinator.render(g, world);
    }

    @Override
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            game.getStateRegistry().setState("pause");
        }

        if (code == KeyEvent. VK_V) {
            coordinator.getCameraSystem().toggleSpectator();
        }

        if (coordinator.getCameraSystem().isSpectatorMode()) {
            if (code == KeyEvent.VK_LEFT) coordinator.getCameraSystem().moveLeft();
            if (code == KeyEvent.VK_RIGHT) coordinator.getCameraSystem().moveRight();
        }
    }

    @Override
    public void keyReleased(int code) {}

    public void reset() {
        playerFell = false;
        attackKeyPressed = false;
        lightningKeyPressed = false;
    }

    public Player getPlayer() { return player; }
}