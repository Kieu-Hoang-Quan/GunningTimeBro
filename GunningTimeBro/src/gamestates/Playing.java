package gamestates;

import java.awt.Color;
import java.awt.Graphics;

public class Playing extends GameState {

    public Playing(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update() {
        // Update nhân vật, quái, map, va chạm...
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawString("PLAYING STATE - Press ESC to Menu", 100, 100);
    }

    @Override
    public void handleInput() {
        if (Input.isKeyPressed(Input.ESC)) {
            gsm.setState(GameStateManager.MENU);
        }
    }
}
