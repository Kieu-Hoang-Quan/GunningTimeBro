package main;

import entity.Player;
import entity.Entity;

import gamestates.*;
import map.*;

import java.awt.Graphics;
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


    // ADD THESE MAP COMPONENTS
    private BgManager bgManager;
    private TileMap tileMap;
    private int cameraX = 0;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
//    public final static int GAME_WIDTH = 576;
//    public final static int GAME_HEIGHT = 324;

    //
    public Game() {
        initClasses();

        // Khởi tạo panel & window
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    // Getter cho các state & player
    public Player getPlayer() { return player; }
    public Menu getMenu() { return menu; }
    public Playing getPlaying() { return playing; }

    private void initClasses() {
        // INITIALIZE MAP first
        try {
            initMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Then spawn player on the ground
        float spawnX = 3 * TILES_SIZE; // 3 tiles from left edge
        float spawnY = findGroundY(spawnX); // Find ground at that X position

        player = new Player(0, 0, (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLvlData(LevelTileConfig.createLevelGrid());
    }


    // Helper method to find ground level
    private float findGroundY(float x) {
        // In WORLD coordinates, ground row 9 is simply at row * TILES_SIZE
        int groundRow = 9;
        // Player hitbox height is 15 * SCALE, spawn just above ground tile
        return groundRow * TILES_SIZE - (15 * SCALE);
    }

    private void initMap() throws IOException {
        // Setup background layers
        bgManager = new BgManager();
        LevelBackgroundConfig.setupFactoryBackground(bgManager);

        // Setup tile map
        TileSet tileSet = LevelTileConfig.createTileSet();
        int[][] levelGrid = LevelTileConfig.createLevelGrid();
        tileMap = new TileMap(levelGrid, tileSet);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.currentState) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            default:
                break;
        }
    }

    public void render(Graphics g) {
        switch (GameState.currentState) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                player.render(g);
                break;
            default:
                break;
        }
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
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;

            }
        }

    }

    // Getter cho GamePanel nếu muốn Menu/Playing truy cập
    public GamePanel getGamePanel() { return gamePanel; }
}