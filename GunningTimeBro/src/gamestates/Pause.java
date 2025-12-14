package gamestates;

import main.Game;
import ui.MenuUI;

import java.awt.*;
import java.awt.event. KeyEvent;

public class Pause extends State {

    private final String[] options = {"Resume", "Restart", "Exit to Menu"};
    private int currentSelection = 0;
    private float animationOffset = 0;
    private MenuUI menuUI;
    private Playing playingState;

    public Pause(Game game, Playing playingState) {
        super(game);
        this.playingState = playingState;
        this. menuUI = new MenuUI("PAUSED", options);
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
        playingState.draw(g);

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game. GAME_HEIGHT);

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
            case KeyEvent.VK_S:
                navigateDown();
                break;

            case KeyEvent.VK_ENTER:
                handleSelection();
                break;

            case KeyEvent.VK_ESCAPE:
                game.getStateRegistry().setState("playing");
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
        if (currentSelection >= options.length) {
            currentSelection = 0;
        }
    }

    private void handleSelection() {
        switch (currentSelection) {
            case 0:
                game.getStateRegistry().setState("playing");
                break;
            case 1:
                game.restartGame();
                break;
            case 2:
                game.getStateRegistry().setState("menu");
                break;
        }
    }

    @Override
    public void keyReleased(int code) {
    }
}