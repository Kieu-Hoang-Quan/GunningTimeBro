package main;

import gamestates.GameStateManager;
import map.LevelTileConfig;
import entity.player.Player;
import entity.enemy.EnemyManager;

import java.awt.*;

/**
 * Game - Main class.
 *
 * SOLID:
 * - SRP: Chỉ lo game loop và điều phối
 * - Delegate mọi thứ cho GameContext
 */
public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;

    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private GameContext context;

    // Constants
    public static final int TILES_DEFAULT_SIZE = 32;
    public static final float SCALE = 2f;
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;
    public static final int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {
        System.out.println("[Game] Starting initialization...");

        try {
            // Build context - context sẽ được gán trước khi được sử dụng
            context = new GameContextBuilder().build(this);
            System.out.println("[Game] Context built successfully");
        } catch (Exception e) {
            System.err.println("[Game] Failed to build GameContext");
            e.printStackTrace();
            throw new RuntimeException("Failed to build GameContext", e);
        }

        // Tạo UI components
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        // Start game loop
        startGameLoop();

        System.out.println("[Game] Initialization complete");
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        context.stateRegistry.getStateManager().update();

        if (isPlaying()) {
            context.healthBarUI.update();
            context.itemNotificationUI.update(1f / 60f);

            if (! context.player.getHealthComponent().isAlive()) {
                context.stateRegistry.setState("gameOver");
            }
        }
    }

    public void render(Graphics2D g) {
        context.stateRegistry.getStateManager().render(g);

        if (isPlaying()) {
            context.healthBarUI. render(g);
            context. itemNotificationUI.render(g);
        }
    }

    private boolean isPlaying() {
        return context.stateRegistry.getCurrentState() == context.stateRegistry.getPlaying();
    }

    public void restartGame() {
        System.out.println("[Game] Restarting.. .");

        float spawnX = 3 * TILES_SIZE;
        float spawnY = 300;

        // Tạo Player mới
        Player newPlayer = new Player(spawnX, spawnY,
                (int)(64 * SCALE), (int)(40 * SCALE));
        newPlayer.loadLvlData(LevelTileConfig.createLevelGrid());

        // Tạo EnemyManager mới
        EnemyManager newEnemyManager = new EnemyManager(this);
        newEnemyManager.loadEnemies(LevelTileConfig. createLevelGrid());
        newPlayer.setEnemies(newEnemyManager.getEnemies());

        // Reset items
        context.world.getItemManager().clear();
        context.world.getItemManager().loadItemsForLevel(1);

        // Update context
        context.updateEntities(newPlayer, newEnemyManager);

        // Restart states
        context.stateRegistry. restartPlaying(newPlayer, newEnemyManager);

        System.out.println("[Game] Restart complete");
    }

    public void windowFocusLost() {
        context.player.resetDirBooleans();
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        double deltaU = 0, deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) { update(); deltaU--; }
            if (deltaF >= 1) { gamePanel.repaint(); deltaF--; }
        }
    }

    // ===== Getters =====
    public GameContext getContext() { return context; }
    public Player getPlayer() { return context.player; }
    public EnemyManager getEnemyManager() { return context.enemyManager; }
    public gamestates.GameStateRegistry getStateRegistry() { return context.stateRegistry; }
    public inputs.InputManager getInputManager() { return context.inputManager; }
    public GamePanel getGamePanel() { return gamePanel; }
    public GameStateManager getStateManager() { return context.stateRegistry.getStateManager(); }
    public world.World getWorld() { return context.world; }
}