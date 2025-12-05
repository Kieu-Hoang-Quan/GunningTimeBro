package gamestates;

import main.Game;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Menu extends State {

    private final String[] options = {"Play", "Quit"};
    private int currentSelection = 0;
    private float animationOffset = 0;

    public Menu(Game game) {
        super(game);
    }

    @Override
    public void update() {
        // Animation cho arrow indicator
        animationOffset += 0.1f;
        if (animationOffset > Math.PI * 2) {
            animationOffset = 0;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // Enable anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background gradient
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(20, 20, 40),
                0, Game.GAME_HEIGHT, new Color(40, 20, 60)
        );
        g.setPaint(gradient);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // Title
        drawTitle(g);

        // Menu options
        drawOptions(g);

        // Footer hint
        drawHint(g);
    }

    private void drawTitle(Graphics2D g) {
        Font titleFont = new Font("Arial", Font.BOLD, 64);
        g.setFont(titleFont);

        String title = "Gunning Time Bro!";
        FontMetrics metrics = g.getFontMetrics(titleFont);
        int titleX = (Game.GAME_WIDTH - metrics.stringWidth(title)) / 2;
        int titleY = 120;

        // Shadow
        g.setColor(new Color(0, 0, 0, 100));
        g.drawString(title, titleX + 3, titleY + 3);

        // Main title
        g.setColor(new Color(100, 200, 255));
        g.drawString(title, titleX, titleY);

        // Underline effect
        g.setColor(new Color(100, 200, 255, 150));
        g.setStroke(new BasicStroke(3));
        g.drawLine(titleX, titleY + 10, titleX + metrics.stringWidth(title), titleY + 10);
    }

    private void drawOptions(Graphics2D g) {
        Font optionFont = new Font("Arial", Font.BOLD, 36);
        g.setFont(optionFont);
        FontMetrics metrics = g.getFontMetrics(optionFont);

        int startY = 280;
        int spacing = 70;

        for (int i = 0; i < options.length; i++) {
            int optionX = (Game.GAME_WIDTH - metrics.stringWidth(options[i])) / 2;
            int optionY = startY + i * spacing;

            if (i == currentSelection) {
                // Highlight box
                int boxPadding = 20;
                int boxWidth = metrics.stringWidth(options[i]) + boxPadding * 2;
                int boxHeight = metrics.getHeight();
                int boxX = optionX - boxPadding;
                int boxY = optionY - metrics.getAscent();

                // Box background
                g.setColor(new Color(255, 100, 100, 80));
                g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

                // Box border
                g.setColor(new Color(255, 150, 150));
                g.setStroke(new BasicStroke(3));
                g.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

                // Animated arrow
                int arrowOffset = (int)(Math.sin(animationOffset) * 5);
                drawArrow(g, optionX - 50 + arrowOffset, optionY - 8);

                // Selected text
                g.setColor(Color.WHITE);
            } else {
                // Normal text
                g.setColor(new Color(180, 180, 200));
            }

            g.drawString(options[i], optionX, optionY);
        }
    }

    private void drawArrow(Graphics2D g, int x, int y) {
        int[] xPoints = {x, x + 15, x};
        int[] yPoints = {y, y + 8, y + 16};

        g.setColor(new Color(255, 200, 100));
        g.fillPolygon(xPoints, yPoints, 3);

        g.setColor(new Color(255, 150, 50));
        g.setStroke(new BasicStroke(2));
        g.drawPolygon(xPoints, yPoints, 3);
    }

    private void drawHint(Graphics2D g) {
        Font hintFont = new Font("Arial", Font.ITALIC, 18);
        g.setFont(hintFont);

        String hint = "↑↓ Navigate  •  ENTER Select";
        FontMetrics metrics = g.getFontMetrics(hintFont);
        int hintX = (Game.GAME_WIDTH - metrics.stringWidth(hint)) / 2;

        g.setColor(new Color(150, 150, 180, 200));
        g.drawString(hint, hintX, Game.GAME_HEIGHT - 40);
    }

    @Override
    public void keyPressed(int code) {
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            currentSelection--;
            if (currentSelection < 0) currentSelection = options.length - 1;
        }

        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            currentSelection++;
            if (currentSelection >= options.length) currentSelection = 0;
        }

        if (code == KeyEvent.VK_ENTER) {
            if (currentSelection == 0) {
                game.getStateManager().setState(game.getPlaying());
            } else if (currentSelection == 1) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(int code) {}
}