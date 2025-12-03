package gamestates;

import java.awt.Graphics;
import main.Game;

public abstract class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public abstract void update();
    public abstract void draw(Graphics g);
    public abstract void keyPressed(int code);
    public abstract void keyReleased(int code);
}
