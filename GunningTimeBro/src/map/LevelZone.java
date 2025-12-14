package map;
import java.awt.geom.Rectangle2D;
public class LevelZone {
    private final Rectangle2D.Float bounds;
    private final String levelName;

    public LevelZone(float x, float y, float width, float height, String levelName) {
        this.bounds = new Rectangle2D.Float(x, y, width, height);
        this.levelName = levelName;
    }

    public boolean contains(Rectangle2D.Float playerHitbox) {
        return bounds.intersects(playerHitbox);
    }

    public String getLevelName() {
        return levelName;
    }
}
