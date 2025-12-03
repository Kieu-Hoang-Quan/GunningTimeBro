package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import main.Game;

public class Menu extends State {

    private String[] options = {"Play", "Quit"};
    private int currentSelection = 0;

    public Menu(Game game) {
        super(game);
    }

    @Override
    public void update() {
        // menu animation
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("MENU", Game.GAME_WIDTH / 2 - 20, 200);

        for (int i = 0; i < options.length; i++) {
            g.setColor(i == currentSelection ? Color.RED : Color.WHITE);
            g.drawString(options[i], Game.GAME_WIDTH / 2 - 30, 300 + i * 40);
        }
    }

    // ⚠ phải implement đúng signature int code
    @Override
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_UP) {
            currentSelection--;
            if (currentSelection < 0) currentSelection = options.length - 1;
        } else if (code == KeyEvent.VK_DOWN) {
            currentSelection++;
            if (currentSelection >= options.length) currentSelection = 0;
        } else if (code == KeyEvent.VK_ENTER) {
            if (currentSelection == 0) GameState.changeState(GameState.PLAYING);
            else if (currentSelection == 1) System.exit(0);
        }
    }

    @Override
    public void keyReleased(int code) {
        // Không cần xử lý
    }
}
