package gamestates;

import main.Game;
import world.World;
import entity.player.Player;
import entity.enemy.EnemyManager;
import inputs.InputManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized state management
 *
 * SOLID:
 * - SRP:  Chỉ quản lý states
 * - OCP: Thêm state mới không cần sửa code cũ
 * - DIP: Nhận dependencies qua constructor
 */
public class GameStateRegistry {

    private final Game game;
    private final World world;
    private final InputManager inputManager;

    // Dependencies cần cho states
    private Player player;
    private EnemyManager enemyManager;

    private final Map<String, State> states;
    private final GameStateManager stateManager;

    public GameStateRegistry(Game game, World world, InputManager inputManager,
                             Player player, EnemyManager enemyManager) {
        this.game = game;
        this.world = world;
        this.inputManager = inputManager;
        this.player = player;
        this.enemyManager = enemyManager;

        this.states = new HashMap<>();
        this.stateManager = new GameStateManager();

        registerAllStates();
    }

    private void registerAllStates() {
        registerState("menu", new Menu(game));

        // Tạo Playing với dependencies trực tiếp
        Playing playing = new Playing(game, world, player, enemyManager, inputManager);
        registerState("playing", playing);

        registerState("pause", new Pause(game, playing));
        registerState("gameOver", new GameOver(game));
    }

    private void registerState(String name, State state) {
        states.put(name, state);
        System.out.println("[StateRegistry] Registered: " + name);
    }

    public State getState(String name) {
        return states.get(name);
    }

    public void setState(String name) {
        State state = states.get(name);
        if (state != null) {
            stateManager.setState(state);
        } else {
            System.err.println("[StateRegistry] State not found: " + name);
        }
    }

    /**
     * Restart với Player và EnemyManager mới
     */
    public void restartPlaying(Player newPlayer, EnemyManager newEnemyManager) {
        this.player = newPlayer;
        this.enemyManager = newEnemyManager;

        states.remove("playing");
        states.remove("pause");

        Playing newPlaying = new Playing(game, world, player, enemyManager, inputManager);
        Pause newPause = new Pause(game, newPlaying);

        registerState("playing", newPlaying);
        registerState("pause", newPause);

        setState("playing");
    }

    // Convenience getters
    public Menu getMenu() { return (Menu) states.get("menu"); }
    public Playing getPlaying() { return (Playing) states.get("playing"); }
    public Pause getPause() { return (Pause) states.get("pause"); }
    public GameOver getGameOver() { return (GameOver) states.get("gameOver"); }

    public GameStateManager getStateManager() { return stateManager; }
    public State getCurrentState() { return stateManager.getCurrentState(); }
}