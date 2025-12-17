package inputs;

import java.awt.event. KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized input manager.
 * SOLID: SRP - Only manages input state
 */
public class InputManager {
    private Map<Integer, Boolean> keyStates;


    public enum Action {
        MOVE_LEFT,
        MOVE_RIGHT,
        JUMP,
        CROUCH,
        ATTACK,
        LIGHTNING,
        RELOAD,
        MENU,
        SELECT,
        NAVIGATE_UP,
        NAVIGATE_DOWN
    }

    private Map<Integer, Action> keyBindings;

    public InputManager() {
        keyStates = new HashMap<>();
        keyBindings = new HashMap<>();
        initDefaultBindings();
    }


    private void initDefaultBindings() {
        // Movement
        keyBindings.put(KeyEvent.VK_A, Action.MOVE_LEFT);
        keyBindings.put(KeyEvent.VK_LEFT, Action.MOVE_LEFT);

        keyBindings.put(KeyEvent.VK_D, Action.MOVE_RIGHT);
        keyBindings.put(KeyEvent.VK_RIGHT, Action. MOVE_RIGHT);

        keyBindings.put(KeyEvent.VK_SPACE, Action.JUMP);
        keyBindings.put(KeyEvent.VK_W, Action.JUMP);
        keyBindings.put(KeyEvent. VK_UP, Action.JUMP);

        keyBindings.put(KeyEvent.VK_S, Action.CROUCH);
        keyBindings.put(KeyEvent.VK_DOWN, Action.CROUCH);

        // Combat
        keyBindings.put(KeyEvent.VK_J, Action.ATTACK);
        keyBindings.put(KeyEvent.VK_K, Action.LIGHTNING);
        keyBindings.put(KeyEvent.VK_R, Action.RELOAD);

        // Menu
        keyBindings.put(KeyEvent.VK_ESCAPE, Action.MENU);
        keyBindings.put(KeyEvent.VK_ENTER, Action.SELECT);
    }

    public void keyPressed(int keyCode) {
        keyStates.put(keyCode, true);
    }


    public void keyReleased(int keyCode) {
        keyStates.put(keyCode, false);
    }


    public boolean isActionActive(Action action) {
        for (Map.Entry<Integer, Action> entry : keyBindings.entrySet()) {
            if (entry.getValue() == action) {
                Boolean state = keyStates.get(entry.getKey());
                if (state != null && state) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isActionJustPressed(Action action) {
        return isActionActive(action);
    }

    public void reset() {
        keyStates. clear();
    }


    public void rebindKey(int keyCode, Action action) {
        keyBindings.put(keyCode, action);
    }
}