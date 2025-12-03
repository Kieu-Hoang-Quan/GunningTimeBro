package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.*;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (GameState.currentState) {
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(code);
                break;

            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(code);
                break;

            case OPTIONS:
                // Nếu chưa có Options, để trống
                break;

            case QUIT:
                System.exit(0);
                break;

            default:
                break;
        }    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (GameState.currentState) {
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(code);
                break;

            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(code);
                break;

            case OPTIONS:
                // Nếu có Options, xử lý ở đây
                break;

            default:
                break;
        }
    }
}