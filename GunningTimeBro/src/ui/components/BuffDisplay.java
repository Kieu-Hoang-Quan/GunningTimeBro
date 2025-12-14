package ui.components;

import entity.player.Player;
import java.awt.*;

/**
 * SINGLE RESPONSIBILITY: Hiển thị active buffs ✅
 */
public class BuffDisplay {

    private Player player;
    private int x;
    private int y;

    public BuffDisplay(Player player, int x, int y) {
        this.player = player;
        this. x = x;
        this. y = y;
    }

    public void render(Graphics2D g) {
        g.setFont(new Font("Arial", Font.BOLD, 14));

        int currentY = y;

        // Damage Boost
        if (player.getBuffSystem().hasBuff("DAMAGE_BOOST")) {
            renderDamageBoost(g, currentY);
            currentY += 20;  // Next buff below
        }

        // ✅ Easy to add more buffs here
    }

    private void renderDamageBoost(Graphics2D g, int yPos) {
        float remainingTime = player.getBuffSystem().getRemainingTime("DAMAGE_BOOST");
        float boostValue = player.getBuffSystem().getBuffValue("DAMAGE_BOOST");

        g.setColor(new Color(255, 100, 100));
        g.drawString("Damage Boost: +" + (int)(boostValue * 100) + "% (" +
                String.format("%.1f", remainingTime) + "s)", x, yPos);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}