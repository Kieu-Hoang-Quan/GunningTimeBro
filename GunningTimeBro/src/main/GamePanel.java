package main;

import inputs.*;
import java.awt.*;
import javax.swing. JPanel;

public class GamePanel extends JPanel {

    private Game game;
    private KeyboardInputs keyboardInputs;

    public static Game gameInstance;

    public GamePanel(Game game) {
        System.out.println("[GamePanel] Creating panel...");
        this.game = game;

        GamePanel.gameInstance = game;

        System.out.println("[GamePanel] Setting panel size.. .");
        setPanelSize();

        System.out.println("[GamePanel] Initializing keyboard inputs...");
        this.keyboardInputs = new KeyboardInputs(this, game. getInputManager());
        addKeyListener(keyboardInputs);
        setFocusable(true);
        requestFocusInWindow();

        System.out.println("[GamePanel] Panel created ✓");
    }

    private void setPanelSize() {
        Dimension size = new Dimension(Game.GAME_WIDTH, Game. GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("[GamePanel] Size set to " + Game.GAME_WIDTH + "x" + Game.GAME_HEIGHT);
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