package gamestates;

import java.awt.*;

import main.Game;

public abstract class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g);
    public abstract void keyPressed(int code);
    public abstract void keyReleased(int code);
}
