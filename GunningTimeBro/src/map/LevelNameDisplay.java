package map;
import java.awt.*;
public class LevelNameDisplay implements  LevelZoneListener {
    private String currentText = "";
    private int timer = 0;
    private static final int DISPLAY_TIME = 120; // frames (~2s)

    @Override
    public void onEnterZone(String levelName) {
        this.currentText = levelName;
        this.timer = DISPLAY_TIME;
    }

    public void update() {
        if (timer > 0) timer--;
    }

    public void render(Graphics2D g) {
        if (timer <= 0) return;

        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.setColor(Color.WHITE);

        FontMetrics fm = g.getFontMetrics();
        int x = (800 - fm.stringWidth(currentText)) / 2;
        int y = 100;

        g.drawString(currentText, x, y);
    }
}
