package Gun;
import java.awt.*;
public class PickupItem {
    public float x, y;
    public boolean taken = false;

    public PickupItem(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void render(Graphics2D g, int camX) {
        if (taken) return;
        g.setColor(Color.CYAN);
        g.fillRect((int)(x - camX), (int)y, 20, 20);
    }
}
