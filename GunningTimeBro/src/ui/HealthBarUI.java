package ui;

import entity.player.Player;
import java.awt.*;

public class HealthBarUI {
    private Player player;

    public HealthBarUI(Player player) {
        this.player = player;
    }

    public void update() {
    }

    public void render(Graphics2D g) {
        int barX = 20;
        int barY = 20;
        int barWidth = 200;
        int barHeight = 20;

        // ✅ FIX: Use getHealth() instead of getCurrentHealth()
        float healthPercent = player.getHealthComponent().getHealth() /
                player.getHealthComponent().getMaxHealth();

        g.setColor(Color. DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        g.setColor(Color.RED);
        g.fillRect(barX, barY, (int)(barWidth * healthPercent), barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, barHeight);

        // ✅ FIX:  Use getHealth() here too
        String hpText = (int)player.getHealthComponent().getHealth() + "/" +
                (int)player.getHealthComponent().getMaxHealth();
        g.drawString(hpText, barX + barWidth / 2 - 20, barY + 15);

        int lightningCharges = player. getAbilitySystem().getLightningCharges();
        drawLightningCharges(g, barX, barY + 30, lightningCharges);

        drawActiveBuffs(g, barX, barY + 60);
    }

    private void drawLightningCharges(Graphics2D g, int x, int y, int charges) {
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(new Color(255, 215, 0));
        g.drawString("Lightning [K]:  ", x, y);

        for (int i = 0; i < 3; i++) {
            int iconX = x + 120 + (i * 25);

            if (i < charges) {
                g.setColor(new Color(255, 215, 0));
                g.fillOval(iconX, y - 12, 18, 18);
                g.setColor(Color.WHITE);
                g.drawString("*", iconX + 5, y + 2);
            } else {
                g.setColor(Color. GRAY);
                g.drawOval(iconX, y - 12, 18, 18);
            }
        }
    }

    private void drawActiveBuffs(Graphics2D g, int x, int y) {
        g.setFont(new Font("Arial", Font. BOLD, 14));

        if (player.getBuffSystem().hasBuff("DAMAGE_BOOST")) {
            float remainingTime = player.getBuffSystem().getRemainingTime("DAMAGE_BOOST");
            float boostValue = player.getBuffSystem().getBuffValue("DAMAGE_BOOST");

            g.setColor(new Color(255, 100, 100));
            g.drawString("Damage Boost: +" + (int)(boostValue * 100) + "% (" +
                    String.format("%.1f", remainingTime) + "s)", x, y);
        }
    }
}