package map;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class BgLayer {
    private final BufferedImage sprite;
    private final float scrollSpeedX;
    private final int yOffset;

    public BgLayer(BufferedImage sprite, float scrollSpeedX, int yOffset) {
        this.sprite = sprite;
        this.scrollSpeedX = scrollSpeedX;
        this.yOffset = yOffset;
    }

    public void render(Graphics2D g2, int camX, int windowWidth, int windowHeight) {
        if (sprite == null) return;

        int imgW = sprite.getWidth();
        int imgH = sprite.getHeight();
        int drawX = (int) (-camX * scrollSpeedX);

        // Calculate how to scale/position the background to fill the window
        for (int x = drawX; x < windowWidth; x += imgW) {
            // Draw background scaled to window height
            g2.drawImage(sprite, x, 0, imgW, windowHeight, null);
        }
    }
}
