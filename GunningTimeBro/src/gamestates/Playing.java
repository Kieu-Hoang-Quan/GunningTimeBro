package gamestates;

import Gun.Bullet;
import Gun.GunFactory;
import Gun.PickupItem;
import main.Game;
import world.Camera;
import world.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Playing extends State {

    private final Game game;
    private World world;
    private final Camera camera;    // thêm camera
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<PickupItem> pickups = new ArrayList<>();
    private class PickupItem {
        public PickupItem(int i, int i1) {
        }

        public void render(Graphics2D g, int camX) {
        }
    }

    public Playing(Game game, World world) {
        super(game);
        this.game = game;
        this.world = world;
        this.camera = new Camera(); // Playing tự tạo camera
        pickups.add(new PickupItem(500, 300));
        pickups.add(new PickupItem(900, 260));
    }

    @Override
    public void update() {
        game.getPlayer().update();

        // cập nhật camera theo player + map
        camera.update(
                game.getPlayer().getHitbox(),
                world.getTileMap()
        );
        // --- update bullets: remove dead ones, then update each ---
        bullets.removeIf(b -> !b.alive);
        for (gun.Bullet b : bullets) {
            b.update(world.getTileMap());
        }

// --- update pickups: check collision with player hitbox ---
        Rectangle playerHit = game.getPlayer().getHitbox();
// use iterator if you want to remove while iterating safely
        Iterator<gun.PickupItem> it = pickups.iterator();
        while (it.hasNext()) {
            gun.PickupItem p = it.next();
            if (!p.taken && playerHit.intersects(new Rectangle((int)p.x, (int)p.y, p.WIDTH, p.HEIGHT))) {
                p.taken = true;
                // give the player a weapon (replace GunFactory method/package if different)
                game.getPlayer().equipWeapon(GunFactory.createBasicGun(Gun.GunFactory.createBasicGun(game.getGamePanel().getSoundPool()));
                it.remove(); // remove pickup from list
            }
        }
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
        for (PickupItem p : pickups)
            p.render(g, camX);

        for (Bullet b : bullets)
            b.render(g, camX);
    }

    @Override
    public void keyPressed(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE:
                game.getStateManager().setState(game.getMenu());
                break;

            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_SPACE:
                game.getPlayer().setJump(true);
                break;

            case KeyEvent.VK_S:
                game.getPlayer().setDown(true);
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                game.getPlayer().setLeft(true);
                game.getPlayer().setFlip(true);
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                game.getPlayer().setRight(true);
                game.getPlayer().setFlip(false);
                break;
            case KeyEvent.VK_J:
                game.getPlayer().setAttacking(true);
            case KeyEvent.VK_Q:
                Bullet b = game.getPlayer().shoot();
                if (b != null) bullets.add(b);
                break;
        }
    }

    @Override
    public void keyReleased(int code) {
        switch (code) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_SPACE:
                game.getPlayer().setJump(false);
                break;

            case KeyEvent.VK_S:
                game.getPlayer().setDown(false);
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                game.getPlayer().setLeft(false);
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                game.getPlayer().setRight(false);
                break;
        }
    }
}