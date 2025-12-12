package gamestates;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.Rectangle;
import Gun.Bullet;
import Gun.GunFactory;
import Gun.PickupItem;
import Gun.Gun;

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
        public  int WIDTH;
        public boolean taken;
        public Object x;
        public Object y;
        public int HEIGHT;

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
// --- update pickups: check collision with player hitbox ---
        Rectangle2D.Float playerHit = game.getPlayer().getHitbox();
        Iterator<PickupItem> it = pickups.iterator();
        while (it.hasNext()) {
            PickupItem p = it.next();
            // assume PickupItem has public fields x,y and public WIDTH, HEIGHT or getter methods
            if (!p.taken && playerHit.intersects(new Rectangle((int)p.x, (int)p.y, p.WIDTH, p.HEIGHT))) {
                p.taken = true;
                // give the player a weapon
                // ensure GamePanel has getSoundPool() (see step 5)
                Gun gun = GunFactory.createBasicGun(game.getGamePanel().getSoundPool());
                game.getPlayer().equipWeapon(gun);
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