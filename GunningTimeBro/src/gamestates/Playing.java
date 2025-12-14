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
    private boolean lightningKeyPressed = false;  // ✅ Only this (K key)
    private boolean playerFell = false;

    public Playing(Game game, World world, InputManager inputManager) {
        super(game);
        this.world = world;
        this.camera = new Camera();
        this.inputManager = inputManager;
    }

    @Override
    public void update() {
        handlePlayerInput();

        game.getPlayer().update();
        game.getEnemyManager().update();
        world.getItemManager().update(game.getPlayer());

        camera.update(game.getPlayer().getHitbox(), world.getTileMap());

        if (game.getPlayer().getHitbox().y > Game.GAME_HEIGHT + 100) {
            if (! playerFell) {
                game.getPlayer().getHealthComponent().takeDamage(999, null);
                playerFell = true;
            }
        }
    }

    private void handlePlayerInput() {
        game.getPlayer().setLeft(inputManager.isActionActive(Action. MOVE_LEFT));
        game.getPlayer().setRight(inputManager.isActionActive(Action. MOVE_RIGHT));
        game.getPlayer().setJump(inputManager.isActionActive(Action. JUMP));

        boolean attackNow = inputManager.isActionActive(Action.ATTACK);
        if (attackNow && !attackKeyPressed) {
            game.getPlayer().setAttacking(true);
        }
        attackKeyPressed = attackNow;

        boolean lightningNow = inputManager.isActionActive(Action.LIGHTNING);
        if (lightningNow && !lightningKeyPressed) {
            game.getPlayer().useLightningPower();
        }
        lightningKeyPressed = lightningNow;
    }

    @Override
    public void draw(Graphics2D g) {
        int camX = camera.getX();

        world.getBgManager().render(g, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        world.getTileMap().render(g, camX, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        world.getItemManager().render(g, camX);
        game.getEnemyManager().draw(g, camX);
        game.getPlayer().render(g, camX);
    }

    @Override
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            game.getStateRegistry().setState("pause");
        }
    }

    @Override
    public void keyReleased(int code) {
    }

    public void reset() {
        playerFell = false;
        attackKeyPressed = false;
        lightningKeyPressed = false;
    }
}