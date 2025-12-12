package gamestates;

import main.Game;
import world.Camera;
import world.World;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Playing extends State {

    private final Game game;
    private World world;
    private final Camera camera;

    public Playing(Game game, World world) {
        super(game);
        this.game = game;
        this.world = world;
        this.camera = new Camera();
    }

    @Override
    public void update() {
        game.getPlayer().update();

        camera.update(
                game.getPlayer().getHitbox(),
                world.getTileMap()
        );

        // ✔ update enemies
        game.getEnemyManager().update();
    }

    @Override
    public void draw(Graphics2D g) {

        int camX = camera.getX();

        world.getBgManager().render(g, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        world.getTileMap().render(g, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // ✔ vẽ enemies
        game.getEnemyManager().draw(g, camX);

        game.getPlayer().render(g, camX);
    }

    @Override
    public void keyPressed(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> game.getStateManager().setState(game.getMenu());
            case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> game.getPlayer().setJump(true);
            case KeyEvent.VK_S -> game.getPlayer().setDown(true);
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                game.getPlayer().setLeft(true);
                game.getPlayer().setFlip(true);
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                game.getPlayer().setRight(true);
                game.getPlayer().setFlip(false);
            }
            case KeyEvent.VK_J -> game.getPlayer().setAttacking(true);
        }
    }

    @Override
    public void keyReleased(int code) {
        switch (code) {
            case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_SPACE -> game.getPlayer().setJump(false);
            case KeyEvent.VK_S -> game.getPlayer().setDown(false);
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> game.getPlayer().setLeft(false);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> game.getPlayer().setRight(false);
        }
    }
}