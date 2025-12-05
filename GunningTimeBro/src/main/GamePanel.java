package main;

import java.awt.*;
import javax.swing.JPanel;

import inputs.KeyboardInputs;

public class GamePanel extends JPanel {

    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();

        addKeyListener(new KeyboardInputs(this));
        setFocusable(true);
        requestFocusInWindow();
    }

    private void setPanelSize() {
        setPreferredSize(new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g2) {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D) g2;

        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
