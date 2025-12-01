package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import gamestates.GameState;
import inputs.KeyboardInputs;

public class GamePanel extends JPanel {

    public final int GAME_WIDTH = 1280;
    public final int GAME_HEIGHT = 800;

    private Game game;

    public GamePanel(Game game) {
        this.game = game;

        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.addKeyListener(new KeyboardInputs(this));
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (GameState.currentState) {
            case MENU:
                game.getMenu().draw(g);
                break;
            case PLAYING:
                game.getPlaying().draw(g);
                break;
            case OPTIONS:
                break;
        }
    }

    public Game getGame() { return game; }
}
