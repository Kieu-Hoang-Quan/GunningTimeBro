package main;

import gamestates.GameStateManager;
import inputs.InputManager;
import world.*;
import entity.player.Player;
import entity.enemy.EnemyManager;
import gamestates.GameStateRegistry;
import map.*;
import ui.*;

import java.awt.*;
import java.io.IOException;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;

    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;
    private EnemyManager enemyManager;
    private World world;
    private InputManager inputManager;

    private GameStateRegistry stateRegistry;

    private HealthBarUI healthBarUI;
    private ItemNotificationUI itemNotificationUI;

    public static final int TILES_DEFAULT_SIZE = 32;
    public static final float SCALE = 2f;
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {
        System.out.println("[Game] ========== STARTING GAME ==========");
        initClasses();
        System.out.println("[Game] ========== CREATING WINDOW ==========");
        gamePanel = new GamePanel(this);
        System.out.println("[Game] GamePanel created");
        gameWindow = new GameWindow(gamePanel);
        System.out.println("[Game] GameWindow created");
        gamePanel.requestFocus();
        System.out.println("[Game] Focus requested");
        startGameLoop();
        System.out. println("[Game] ========== GAME STARTED ==========");
    }

    private void initClasses() {
        System.out.println("[Game] Initialization started");

        System.out.println("[Game] Creating InputManager.. .");
        inputManager = new InputManager();

        System.out.println("[Game] Creating World...");
        try {
            world = new World();
        } catch (IOException e) {
            System.err.println("[Game] FATAL: World creation failed!");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("[Game] World created successfully");

        System.out.println("[Game] Creating Player...");
        float spawnX = 3 * TILES_SIZE;
        float spawnY = 300;
        player = new Player(spawnX, spawnY, (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLvlData(LevelTileConfig.createLevelGrid());
        System.out.println("[Game] Player created");

        System.out.println("[Game] Creating EnemyManager.. .");
        enemyManager = new EnemyManager(this);
        System.out.println("[Game] EnemyManager instance created");

        System.out. println("[Game] Loading enemies...");
        enemyManager.loadEnemies(LevelTileConfig.createLevelGrid());
        System.out.println("[Game] Enemies loaded");

        System.out. println("[Game] Setting player enemies...");
        player.setEnemies(enemyManager.getEnemies());
        System.out.println("[Game] Player enemies set");

        System.out. println("[Game] Creating UI...");
        healthBarUI = new HealthBarUI(player);
        System.out.println("[Game] HealthBarUI created");

        itemNotificationUI = new ItemNotificationUI();
        System.out.println("[Game] ItemNotificationUI created");

        System.out.println("[Game] Creating StateRegistry...");
        stateRegistry = new GameStateRegistry(this, world, inputManager);
        System.out. println("[Game] StateRegistry created");

        System.out.println("[Game] Setting initial state to menu...");
        stateRegistry. setState("menu");
        System.out. println("[Game] Initial state set");

        System.out.println("[Game] Initialization complete ✓");
    }

    private void startGameLoop() {
        System.out.println("[Game] Creating game thread...");
        gameThread = new Thread(this);
        System.out.println("[Game] Starting thread.. .");
        gameThread.start();
        System.out.println("[Game] Thread started ✓");
    }

    public void update() {
        stateRegistry.getStateManager().update();

        if (stateRegistry.getCurrentState() == stateRegistry.getPlaying()) {
            healthBarUI.update();
            itemNotificationUI.update(1f / 60f);

            if (! player.getHealthComponent().isAlive()) {
                stateRegistry.setState("gameOver");
            }
        }
    }

    public void render(Graphics2D g) {
        stateRegistry.getStateManager().render(g);

        if (stateRegistry.getCurrentState() == stateRegistry.getPlaying()) {
            healthBarUI.render(g);
            itemNotificationUI.render(g);
        }
    }

    public void restartGame() {
        System.out.println("[Game] ========== RESTARTING GAME ==========");

        float spawnX = 3 * TILES_SIZE;
        float spawnY = 300;

        // Reset player
        System.out.println("[Game] Resetting player...");
        player = new Player(spawnX, spawnY, (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLvlData(LevelTileConfig.createLevelGrid());

        // Reset enemies
        System. out.println("[Game] Resetting enemies...");
        enemyManager = new EnemyManager(this);
        enemyManager.loadEnemies(LevelTileConfig.createLevelGrid());
        player.setEnemies(enemyManager.getEnemies());

        // ✅ FIX: Reset items!
        System.out.println("[Game] Resetting items...");
        world.getItemManager().clear();
        world.getItemManager().loadItemsForLevel(1);

        // Reset UI
        System.out.println("[Game] Resetting UI...");
        healthBarUI = new HealthBarUI(player);
        itemNotificationUI = new ItemNotificationUI();

        // Reset states through registry
        System.out.println("[Game] Resetting states...");
        stateRegistry.restartPlaying();

        System.out. println("[Game] ========== RESTART COMPLETE ==========");
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    @Override
    public void run() {
        System.out.println("[GameLoop] ========== GAME LOOP STARTED ==========");

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0;
        double deltaF = 0;

        System.out.println("[GameLoop] Entering main loop...");

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

    public Player getPlayer() { return player; }
    public EnemyManager getEnemyManager() { return enemyManager; }
    public GameStateRegistry getStateRegistry() { return stateRegistry; }
    public InputManager getInputManager() { return inputManager; }
    public GamePanel getGamePanel() { return gamePanel; }
    public GameStateManager getStateManager() {
        return stateRegistry.getStateManager();
    }
    public World getWorld() {
        return world;
    }
}