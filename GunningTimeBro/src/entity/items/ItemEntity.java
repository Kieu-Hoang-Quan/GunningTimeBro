package entity.items;

import entity.player.Player;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ItemEntity {
    private float x, y;
    private Rectangle2D. Float hitbox;
    private Item item;
    private BufferedImage sprite;
    private boolean collected = false;

    private float floatOffset = 0;
    private float floatSpeed = 0.05f;

    private static final int ITEM_SIZE = (int)(32 * Game. SCALE);

    public ItemEntity(float x, float y, Item item, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.item = item;
        this.sprite = sprite;
        this.hitbox = new Rectangle2D.Float(x, y, ITEM_SIZE, ITEM_SIZE);
    }

    public void update(Player player) {
        if (collected) return;

        floatOffset += floatSpeed;
        if (floatOffset > Math.PI * 2) {
            floatOffset = 0;
        }

        if (hitbox.intersects(player.getHitbox())) {
            item.apply(player);
            collected = true;
            // ✅ Now can use getName() from interface
            System.out.println("[Item] Collected:  " + item.getName());
        }
    }

    public void render(Graphics g, int camX) {
        if (collected) return;

        Graphics2D g2d = (Graphics2D) g;

        float renderY = y + (float)(Math.sin(floatOffset) * 5);
        int screenX = (int)(x - camX);
        int screenY = (int)renderY;

        drawGlow(g2d, screenX, screenY);

        if (sprite != null) {
            g2d.drawImage(sprite, screenX, screenY, ITEM_SIZE, ITEM_SIZE, null);
        } else {
            g2d. setColor(new Color(255, 200, 50));
            g2d.fillRect(screenX, screenY, ITEM_SIZE, ITEM_SIZE);

            // ✅ Can show item name on fallback
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 8));
            String initial = item.getName().substring(0, 1);
            g2d.drawString(initial, screenX + 12, screenY + 20);
        }
    }

    private void drawGlow(Graphics2D g2d, int x, int y) {
        Composite originalComposite = g2d. getComposite();

        float glowAlpha = 0.2f + (float)(Math.sin(floatOffset * 2) * 0.1f);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite. SRC_OVER, glowAlpha));

        g2d.setColor(new Color(255, 215, 0));
        int glowSize = ITEM_SIZE + 12;
        g2d.fillOval(x - 6, y - 6, glowSize, glowSize);

        g2d.setComposite(originalComposite);
    }

    public boolean isCollected() {
        return collected;
    }

    public Item getItem() {
        return item;
    }
}