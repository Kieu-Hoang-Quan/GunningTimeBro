package main;

import gamestates.Gamestate;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;


    public Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);

    }
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

    }

}
