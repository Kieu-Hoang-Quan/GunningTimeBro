package inputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import main.GamePanel;

public class MouseInputs extends MouseAdapter {

    private final GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gamePanel.addMouseListener(this);
        gamePanel.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // TODO: xử lý click vào Menu hoặc Gameplay
        // Ví dụ:
        // if (GameState.currentState == GameState.MENU) {
        //     gamePanel.getGame().getMenu().handleMouseClick(mouseX, mouseY);
        // }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // TODO: xử lý hover trên Menu buttons
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO: nếu muốn
    }
}
