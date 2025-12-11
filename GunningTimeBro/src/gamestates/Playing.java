package gamestates;

import main.Game;
import world.Camera;
import world.World;
import inputs.InputManager;
import inputs.InputManager.Action;

import java.awt.*;
import java.awt.event.KeyEvent;


public class Playing extends State {

    private World world;
    private final Camera camera;
    private InputManager inputManager;
    private boolean attackKeyPressed = false;

    public Playing(Game game, World world, InputManager inputManager) {
        super(game);
        this.world = world;
        this.camera = new Camera();
        this.inputManager = inputManager;
    }

    @Override
    public void update() {

        handlePlayerInput();

        // Update player
        game.getPlayer().update();

        // Update camera
        camera.update(
                game.getPlayer().getHitbox(),
                world.getTileMap()
        );
    }

    private void handlePlayerInput() {
        // Movement
        game.getPlayer().setLeft(inputManager.isActionActive(Action.MOVE_LEFT));
        game.getPlayer().setRight(inputManager.isActionActive(Action.MOVE_RIGHT));
        game.getPlayer().setJump(inputManager.isActionActive(Action.JUMP));
        boolean attackNow = inputManager.isActionActive(Action.ATTACK);
        if (attackNow && !attackKeyPressed) {
            game.getPlayer().setAttacking(true);
        }
        attackKeyPressed = attackNow;
    }

    @Override
    public void draw(Graphics2D g) {
        int camX = camera.getX();

        world.getBgManager().render(g, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        world.getTileMap().render(g, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        game.getPlayer().render(g, camX);
    }

    @Override
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            game.getStateManager().setState(game.getMenu());
        }
    }

    @Override
    public void keyReleased(int code) {
    }
}