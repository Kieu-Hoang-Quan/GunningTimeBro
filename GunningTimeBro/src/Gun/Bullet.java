package Gun;
import java.awt.*;
import map.TileMap;
import main.Game;
public class Bullet {
    public float x, y;
    public float speedX;
    public boolean alive = true;

    public Bullet(float x, float y, float speedX) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
    }

    public void update(TileMap map) {
        if (!alive) return;

        x += speedX;

        // Collision with map tiles
        int tileSize = Game.TILES_SIZE;
        int gridX = (int)(x / tileSize);
        int gridY = (int)(y / tileSize);

        if (gridY < 0 || gridY >= map.getGrid().length) alive = false;
        else if (gridX < 0 || gridX >= map.getGrid()[0].length) alive = false;
        else if (map.getGrid()[gridY][gridX] != 0) alive = false;
    }

    public void render(Graphics2D g, int camX) {
        if (!alive) return;
        g.setColor(Color.YELLOW);
        g.fillOval((int)(x - camX), (int)y, 8, 8);
    }
}
