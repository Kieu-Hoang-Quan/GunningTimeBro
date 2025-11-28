package gamestates;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

public class GameStateManager {

    public static final int MENU = 0;
    public static final int PLAYING = 1;

    private Map<Integer, GameState> states = new HashMap<>();
    private int currentState;

    public GameStateManager() {
        // Khởi tạo các state
        states.put(MENU, new Menu(this));
        states.put(PLAYING, new Playing(this));

        currentState = MENU;  // Start ở Menu
    }

    public void setState(int state) {
        currentState = state;
    }

    public void update() {
        states.get(currentState).update();
    }

    public void render(Graphics g) {
        states.get(currentState).render(g);
    }

    public void handleInput() {
        states.get(currentState).handleInput();
    }
}
