package gamestates;

import main.Game;
import world.World;
import inputs.InputManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized state management - SOLID compliant
 * Add new states WITHOUT modifying Game.java
 */
public class GameStateRegistry {

    private Game game;
    private World world;
    private InputManager inputManager;

    private Map<String, State> states;
    private GameStateManager stateManager;

    public GameStateRegistry(Game game, World world, InputManager inputManager) {
        this.game = game;
        this.world = world;
        this.inputManager = inputManager;
        this.states = new HashMap<>();
        this.stateManager = new GameStateManager();

        registerAllStates();
    }

    private void registerAllStates() {
        // Register all game states
        registerState("menu", new Menu(game));
        registerState("playing", new Playing(game, world, inputManager));
        registerState("pause", new Pause(game, getPlaying()));
        registerState("gameOver", new GameOver(game));

    }

    private void registerState(String name, State state) {
        states.put(name, state);
        System.out.println("[StateRegistry] Registered:  " + name);
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

    public void restartPlaying() {
        // Remove old states
        states.remove("playing");
        states.remove("pause");

        // Create new instances
        Playing newPlaying = new Playing(game, world, inputManager);
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
    public State getCurrentState() { return stateManager. getCurrentState(); }
}