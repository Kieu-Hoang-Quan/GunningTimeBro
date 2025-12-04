package gamestates;

import main.Game;
import world.Camera;
import world.World;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Playing extends State {

    private final Game game;
    private World world;
    private final Camera camera;    // thêm camera

    public Playing(Game game, World world) {
        super(game);
        this.game = game;
        this.world = world;
        this.camera = new Camera(); // Playing tự tạo camera
    }

    @Override
    public void update() {
        game.getPlayer().update();

        // cập nhật camera theo player + map
        camera.update(
                game.getPlayer().getHitbox(),
                world.getTileMap()
        );
    }

    @Override
    public void draw(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g;

        int camX = camera.getX();

        // vẽ background + tilemap theo camera
        world.getBgManager().render(g2, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        world.getTileMap().render(g2, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // vẽ player
        game.getPlayer().render(g, camX);
    }

    @Override
    public void keyPressed(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE:
                game.getStateManager().setState(game.getMenu());
                break;

            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                game.getPlayer().setJump(true);
                break;

            case KeyEvent.VK_S:
                game.getPlayer().setDown(true);
                break;

            case KeyEvent.VK_A:
                game.getPlayer().setLeft(true);
                game.getPlayer().setFlip(true);
                break;

            case KeyEvent.VK_D:
                game.getPlayer().setRight(true);
                game.getPlayer().setFlip(false);
                break;
        }
    }

    @Override
    public void keyReleased(int code) {
        switch (code) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                game.getPlayer().setJump(false);
                break;

            case KeyEvent.VK_S:
                game.getPlayer().setDown(false);
                break;

            case KeyEvent.VK_A:
                game.getPlayer().setLeft(false);
                break;

            case KeyEvent.VK_D:
                game.getPlayer().setRight(false);
                break;
        }
    }
}
