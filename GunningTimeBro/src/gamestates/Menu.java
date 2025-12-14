package gamestates;

import main.Game;
import ui.MenuUI;

import java.awt.*;
import java. awt.event.KeyEvent;

public class Menu extends State {

    private final String[] options = {"Play", "Quit"};
    private int currentSelection = 0;
    private float animationOffset = 0;
    private final MenuUI menuUI;

    public Menu(Game game) {
        super(game);
        this.menuUI = new MenuUI("Gunning Time Bro!", options);
    }

    @Override
    public void update() {
        updateAnimation();
    }

    private void updateAnimation() {
        animationOffset += 0.1f;
        if (animationOffset > Math.PI * 2) {
            animationOffset = 0;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        menuUI.render(g, currentSelection, animationOffset);
    }

    @Override
    public void keyPressed(int code) {
        switch (code) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                navigateUp();
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent. VK_S:
                navigateDown();
                break;

            case KeyEvent.VK_ENTER:
                handleSelection();
                break;
        }
    }

    private void navigateUp() {
        currentSelection--;
        if (currentSelection < 0) {
            currentSelection = options.length - 1;
        }
    }

    private void navigateDown() {
        currentSelection++;
        if (currentSelection >= options. length) {
            currentSelection = 0;
        }
    }

    private void handleSelection() {
        switch (currentSelection) {
            case 0: // Play
                game.getStateRegistry().setState("playing");
                break;
            case 1: // Quit
                System.exit(0);
                break;
        }
    }

    @Override
    public void keyReleased(int code) {
    }
}