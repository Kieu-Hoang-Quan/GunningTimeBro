package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import gamestates.GameState;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;


public class GamePanel extends JPanel {
    private Game game;

    private static final int SPRITE_WIDTH = 64;
    private static final int SPRITE_HEIGHT = 64;


    public GamePanel(Game game) {

        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
    }
    private void setPanelSize(){
        setPreferredSize(new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT));
        this.addKeyListener(new KeyboardInputs(this));
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);

        switch (GameState.currentState) {
            case MENU:
                game.getMenu().draw(g);
                break;
            case PLAYING:
                game.getPlaying().draw(g);
                break;
            case OPTIONS:
                break;
        }
    }
    public Game getGame() { return game; }
}