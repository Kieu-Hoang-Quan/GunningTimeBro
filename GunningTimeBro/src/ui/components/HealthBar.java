package ui.components;

import entity. player.Player;
import java. awt.*;

public class HealthBar {

    private Player player;
    private int x;
    private int y;
    private int width;
    private int height;

    public HealthBar(Player player, int x, int y, int width, int height) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(Graphics2D g) {
        float healthPercent = player.getHealthComponent().getHealth() /
                player.getHealthComponent().getMaxHealth();

        // Background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);

        // Health fill
        g.setColor(Color.RED);
        g.fillRect(x, y, (int)(width * healthPercent), height);

        // Border
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        // Text
        String hpText = (int)player.getHealthComponent().getHealth() + "/" +
                (int)player.getHealthComponent().getMaxHealth();
        g.drawString(hpText, x + width / 2 - 20, y + 15);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}