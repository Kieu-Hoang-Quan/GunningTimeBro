package ui.components;

import entity.player.Player;
import java.awt.*;

public class LightningChargeDisplay {

    private Player player;
    private int x;
    private int y;

    public LightningChargeDisplay(Player player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics2D g) {
        int charges = player.getAbilitySystem().getLightningCharges();

        // Label
        g.setFont(new Font("Arial", Font. BOLD, 20));
        g.setColor(new Color(255, 215, 0));
        g.drawString("Lightning [K]:  ", x, y);

        // Charge icons
        for (int i = 0; i < 3; i++) {
            int iconX = x + 120 + (i * 25);

            if (i < charges) {
                // Filled charge
                g.setColor(new Color(255, 215, 0));
                g.fillOval(iconX + 15, y - 16, 18, 18);
                g.setColor(Color.WHITE);
            } else {
                // Empty charge
                g.setColor(Color.GRAY);
                g.drawOval(iconX + 15, y - 16, 15, 18);
            }
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}