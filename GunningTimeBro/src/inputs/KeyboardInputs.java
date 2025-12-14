package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import gamestates.*;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;
    private InputManager inputManager;

    public KeyboardInputs(GamePanel gamePanel, InputManager inputManager) {
        this.gamePanel = gamePanel;
        this.inputManager = inputManager;
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (inputManager != null) {
            inputManager.keyPressed(code);
        }
        gamePanel.getGame().getStateManager().keyPressed(code);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (inputManager != null) {
            inputManager.keyReleased(code);
        }
        gamePanel.getGame().getStateManager().keyReleased(code);
    }
}
