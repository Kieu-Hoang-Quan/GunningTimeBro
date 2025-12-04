package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        gamePanel.getGame().getStateManager().keyPressed(code);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        gamePanel.getGame().getStateManager().keyReleased(code);
    }
}
