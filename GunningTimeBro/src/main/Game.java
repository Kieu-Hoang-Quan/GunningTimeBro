package main;

import enity.Player;
import gamestates.Menu;
import gamestates.Playing;
import gamestates.GameState;

import java.awt.Graphics;

public class Game implements Runnable {

    public static final int GAME_WIDTH = 1280;
    public static final int GAME_HEIGHT = 800;

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;
    private Menu menu;
    private Playing playing;

    public Game() {
        // Khởi tạo player và state
        player = new Player(200, 200);
        menu = new Menu(this);
        playing = new Playing(this);

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

        double deltaU = 0, deltaF = 0;
        long lastCheck = System.currentTimeMillis();
        int frames = 0, updates = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) { update(); updates++; deltaU--; }
            if (deltaF >= 1) { gamePanel.repaint(); frames++; deltaF--; }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0; updates = 0;
            }
        }
    }

    // Getter cho GamePanel nếu muốn Menu/Playing truy cập
    public GamePanel getGamePanel() { return gamePanel; }
}
