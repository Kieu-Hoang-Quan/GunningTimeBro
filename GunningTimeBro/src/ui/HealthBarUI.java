package ui;

import entity.player. Player;
import ui.components.*;
import java.awt.*;

/**
 * CLEAN COORDINATOR - Chỉ điều phối các components ✅
 */
public class HealthBarUI {

    private HealthBar healthBar;
    private LightningChargeDisplay lightningDisplay;
    private BuffDisplay buffDisplay;

    public HealthBarUI(Player player) {
        // ✅ Initialize all components with positions
        this.healthBar = new HealthBar(player, 20, 20, 200, 20);
        this.lightningDisplay = new LightningChargeDisplay(player, 20, 60);
        this.buffDisplay = new BuffDisplay(player, 20, 90);
    }

    public void update() {
        // Components update themselves if needed
    }

    public void render(Graphics2D g) {
        healthBar.render(g);
        lightningDisplay.render(g);
        buffDisplay.render(g);
    }
}