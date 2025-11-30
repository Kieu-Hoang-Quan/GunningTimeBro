package main;

import entity.Player;
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

    //
    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }


    private void initClasses() {

        // Then spawn player on the ground
        float spawnX = 3 * TILES_SIZE; // 3 tiles from left edge
        float spawnY = findGroundY(spawnX); // Find ground at that X position

        player = new Player(200, 100, (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLvlData(LevelTileConfig.createLevelGrid());
        // INITIALIZE MAP first
        try {
            initMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // Helper method to find ground level
    private float findGroundY(float x) {
        // The ground row in your level is row 9 (last row)
        // Each row is TILES_SIZE pixels tall
        // GAME_HEIGHT = 324 (from your TileMap. render baseY calculation)
        int groundRow = 9; // Last row of your 10-row grid (0-indexed)
        int rows = 10;
        return 324 - (rows * TILES_SIZE) + (groundRow * TILES_SIZE) - (15 * SCALE);
    }

    private void initMap() throws IOException {
        // Setup background layers
        bgManager = new BgManager();
        LevelBackgroundConfig.setupFactoryBackground(bgManager);

        // Setup tile map
        TileSet tileSet = LevelTileConfig.createTileSet();
        int[][] levelGrid = LevelTileConfig.createLevelGrid();
        tileMap = new TileMap(levelGrid, tileSet);

        // Load collision data ONCE
        player.loadLvlData(levelGrid);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
//        levelManager.update();
        player.update();
        updateCamera();
    }

    private void updateCamera() {
        // Center camera on player
        cameraX = (int) (player.getHitbox().x - GAME_WIDTH / 2);

        // Clamp camera to map bounds
        cameraX = Math.max(0, cameraX);
        cameraX = Math.min(cameraX, tileMap.getMapWidthPixels() - GAME_WIDTH);
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Render background (parallax)
        bgManager.render(g2, cameraX, GAME_WIDTH, GAME_HEIGHT);

        // Render tile map
        tileMap.render(g2, cameraX, GAME_WIDTH, GAME_HEIGHT);
        // Render player (adjust position based on camera)
        g2.translate(-cameraX, 0);
        player.render(g);
        g2.translate(cameraX, 0);
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

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

}