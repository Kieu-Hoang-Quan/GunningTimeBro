package gamestates;

import java.awt.Color;
import java.awt.Graphics;

public class Menu extends GameState {

    public Menu(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update() {
        // Logic menu (animation nền, nút nhấp nháy, ...)
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("MENU - Press ENTER to Play", 100, 100);
    }

    @Override
    public void handleInput() {
        if (Input.isKeyPressed(Input.ENTER)) {
            gsm.setState(GameStateManager.PLAYING);
        }
    }
}
