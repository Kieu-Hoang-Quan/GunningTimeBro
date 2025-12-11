package main;

import inputs.*;
import java.awt.*;
import javax.swing.JPanel;

import inputs.KeyboardInputs;

public class GamePanel extends JPanel {

    private Game game;
    private KeyboardInputs keyboardInputs;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();

        this.keyboardInputs = new KeyboardInputs(this, game.getInputManager());
        addKeyListener(keyboardInputs);
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
