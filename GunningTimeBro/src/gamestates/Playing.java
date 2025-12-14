package gamestates;

import main.Game;
import world.Camera;
import world.World;
import inputs.InputManager;
import inputs.InputManager.Action;
import java.awt.*;
import java.awt.event. KeyEvent;

public class Playing extends State {
    private World world;
    private final Camera camera;
    private InputManager inputManager;

    private boolean attackKeyPressed = false;
    private boolean lightningKeyPressed = false;
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
        game.getPlayer().setJump(inputManager.isActionActive(Action.JUMP));

        boolean attackNow = inputManager.isActionActive(Action.ATTACK);
        if (attackNow && !attackKeyPressed) {
            game.getPlayer().setAttacking(true);
        }
        attackKeyPressed = attackNow;

        boolean lightningNow = inputManager.isActionActive(Action.LIGHTNING);
        if (lightningNow && ! lightningKeyPressed) {
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

        // ✅ ADD:  Hiển thị spectator mode
        if (camera. spectatorMode) {
            drawSpectatorUI(g);
        }
    }

    @Override
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            game.getStateRegistry().setState("pause");
        }

        // ✅ ADD: Bật/tắt spectator mode bằng phím V
        if (code == KeyEvent.VK_V) {
            camera.spectatorMode = ! camera.spectatorMode;
            System.out.println(camera.spectatorMode ? "[Spectator] ON - Use Arrow Keys" : "[Spectator] OFF");
        }

        // ✅ ADD: Di chuyển camera bằng mũi tên (chỉ khi spectator mode)
        if (camera.spectatorMode) {
            if (code == KeyEvent.VK_LEFT) {
                camera.moveCamera(-30, world.getTileMap());
            }
            if (code == KeyEvent.VK_RIGHT) {
                camera.moveCamera(30, world.getTileMap());
            }
        }
    }

    @Override
    public void keyReleased(int code) {
    }

    // ✅ ADD: UI cho spectator mode
    private void drawSpectatorUI(Graphics2D g) {
        g.setColor(new Color(255, 255, 0, 200));
        g.setFont(new Font("Arial", Font. BOLD, 20));
        g.drawString("SPECTATOR MODE - Arrow Keys:  Move | V: Exit", 20, 50);

        // Progress bar
        int mapWidth = world.getTileMap().getMapWidthPixels();
        int maxCamX = Math.max(0, mapWidth - Game.GAME_WIDTH);
        float progress = maxCamX > 0 ? (float)camera.getX() / maxCamX : 0;

        g.setColor(Color.WHITE);
        g.drawString(String.format("Position: %d / %d (%.0f%%)",
                camera.getX(), maxCamX, progress * 100), 20, 80);
    }

    public void reset() {
        playerFell = false;
        attackKeyPressed = false;
        lightningKeyPressed = false;
        camera.spectatorMode = false;  // ✅ ADD
    }
}