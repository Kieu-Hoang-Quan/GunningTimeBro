package map;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class BgManager {
    private final List<BgLayer> layers = new ArrayList<>();

    public void clear() {
        layers.clear();
    }

    public void addLayer(BgLayer layer) {
        layers.add(layer);
    }

    public void render(Graphics2D g2, int camX, int windowWidth, int windowHeight) {
        for (BgLayer layer : layers) {
            layer.render(g2, camX, windowWidth, windowHeight);
        }
    }
}
