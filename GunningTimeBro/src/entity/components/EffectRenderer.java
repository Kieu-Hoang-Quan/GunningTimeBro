package entity. components;

import entity.Entity;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public class EffectRenderer {

    // Effect types
    public enum EffectType {
        INVINCIBILITY,
        DAMAGE_FLASH,
        HEAL_GLOW,
        POWER_UP
    }



    public void renderHealGlow(Graphics2D g, Rectangle2D.Float hitbox, int camX) {
        g.setColor(new Color(0, 255, 0, 100));
        g.fillRect(
                (int)(hitbox.x - camX),
                (int)hitbox.y,
                (int)hitbox.width,
                (int)hitbox.height
        );
    }


    public void renderPowerUpGlow(Graphics2D g, Rectangle2D.Float hitbox, int camX, Color color) {
        // Pulsing glow effect
        int alpha = (int)(100 + Math.sin(System.currentTimeMillis() / 200.0) * 50);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));

        // Draw glow slightly larger than hitbox
        int offset = 5;
        g.fillRect(
                (int)(hitbox.x - camX - offset),
                (int)(hitbox.y - offset),
                (int)(hitbox.width + offset * 2),
                (int)(hitbox.height + offset * 2)
        );
    }

    public void renderEffect(Graphics2D g, Rectangle2D.Float hitbox, int camX,
                             Color color, int alpha, boolean outline) {
        Color effectColor = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                alpha
        );

        g.setColor(effectColor);
        g.fillRect(
                (int)(hitbox.x - camX),
                (int)hitbox.y,
                (int)hitbox.width,
                (int)hitbox.height
        );

        if (outline) {
            g.setColor(new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    Math.min(255, alpha + 100)
            ));
            g.setStroke(new BasicStroke(2));
            g.drawRect(
                    (int)(hitbox.x - camX),
                    (int)hitbox.y,
                    (int)hitbox.width,
                    (int)hitbox.height
            );
        }
    }
}