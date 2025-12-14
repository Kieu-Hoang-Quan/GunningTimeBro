package gamestates;

import java.awt.*;

public class GameStateManager {

    private State currentState;

    public void setState(State newState) {
        currentState = newState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void update() {
        if (currentState != null)
            currentState.update();
    }

    public void render(Graphics2D g) {
        if (currentState != null)
            currentState.draw(g);
    }

    public void keyPressed(int code) {
        if (currentState != null)
            currentState.keyPressed(code);
    }

    public void keyReleased(int code) {
        if (currentState != null)
            currentState.keyReleased(code);
    }
}