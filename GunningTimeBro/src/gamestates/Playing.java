package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import main.Game;

public class Playing extends State {

    private Game game;

    public Playing(Game game) {
        super(game);
        this.game = game;
    }

    @Override
    public void update() {
        game.getPlayer().update();
    }

    @Override
    public void draw(Graphics g) {
        // Background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // Player
        game.getPlayer().render(g);

        // Info
        g.setColor(Color.WHITE);
        g.drawString("PLAYING STATE - Press ESC to return to Menu", 50, 50);
    }

    @Override
    public void keyPressed(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE:
                GameState.changeState(GameState.MENU);
                break;
            case KeyEvent.VK_W: game.getPlayer().setUp(true); break;
            case KeyEvent.VK_S: game.getPlayer().setDown(true); break;
            case KeyEvent.VK_A: game.getPlayer().setLeft(true); break;
            case KeyEvent.VK_D: game.getPlayer().setRight(true); break;
        }
    }

    @Override
    public void keyReleased(int code) {
        switch (code) {
            case KeyEvent.VK_W: game.getPlayer().setUp(false); break;
            case KeyEvent.VK_S: game.getPlayer().setDown(false); break;
            case KeyEvent.VK_A: game.getPlayer().setLeft(false); break;
            case KeyEvent.VK_D: game.getPlayer().setRight(false); break;
        }
    }
}
