package main;

import world.*;
import entity.Player;
import gamestates.*;
import gamestates.Menu;
import map.*;

import java.awt.*;
import java.io.IOException;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;

    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;
    private Menu menu;
    private Playing playing;
    private World world;

    // ✔ StateManager mới
    private GameStateManager gsm;


    public static final int TILES_DEFAULT_SIZE = 32;
    public static final float SCALE = 2f;
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    // Getter cho các state & player
    public Player getPlayer() {
        return player;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public GameStateManager getStateManager() {
        return gsm;
    }


    private void initClasses() {
        // LOAD WORLD
        try {
            world = new World();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // PLAYER
        float spawnX = 3 * TILES_SIZE;
        float spawnY = world.findGroundY(spawnX);

        player = new Player(spawnX, spawnY, (int) (64 * SCALE), (int) (40 * SCALE));

        // STATES
        gsm = new GameStateManager();

        menu = new Menu(this);
        playing = new Playing(this, world);
        player.loadLvlData(LevelTileConfig.createLevelGrid());


        // State mặc định
        gsm.setState(menu);
    }


    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    public void update() {
        gsm.update();
    }

    public void render(Graphics2D g) {
        gsm.render(g);
    }


    public void windowFocusLost() {
        player.resetDirBooleans();
    }


    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;

            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
                lastCheck = System.currentTimeMillis();
            }
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
