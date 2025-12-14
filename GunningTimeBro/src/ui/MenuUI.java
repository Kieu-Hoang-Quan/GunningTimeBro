package ui;

import main.Game;
import java.awt.*;

public class MenuUI {

    private final String title;
    private final String[] options;

    // Colors
    private final Color bgColorTop = new Color(20, 20, 40);
    private final Color bgColorBottom = new Color(40, 20, 60);
    private final Color titleColor = new Color(100, 200, 255);
    private final Color titleShadow = new Color(0, 0, 0, 100);
    private final Color titleUnderline = new Color(100, 200, 255, 150);
    private final Color selectedBg = new Color(255, 100, 100, 80);
    private final Color selectedBorder = new Color(255, 150, 150);
    private final Color selectedText = Color.WHITE;
    private final Color normalText = new Color(180, 180, 200);
    private final Color arrowColor = new Color(255, 200, 100);
    private final Color arrowBorder = new Color(255, 150, 50);
    private final Color hintColor = new Color(150, 150, 180, 200);

    // Fonts
    private final Font titleFont = new Font("Arial", Font.BOLD, 64);
    private final Font optionFont = new Font("Arial", Font.BOLD, 36);
    private final Font hintFont = new Font("Arial", Font.ITALIC, 18);

    // Layout
    private final int titleY = 120;
    private final int optionsStartY = 280;
    private final int optionsSpacing = 70;

    public MenuUI(String title, String[] options) {
        this.title = title;
        this.options = options;
    }

    public void render(Graphics2D g, int currentSelection, float animationOffset) {
        setupRenderingHints(g);
        drawBackground(g);
        drawTitle(g);
        drawOptions(g, currentSelection, animationOffset);
        drawHint(g);
    }


    private void setupRenderingHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }


    private void drawBackground(Graphics2D g) {
        GradientPaint gradient = new GradientPaint(
                0, 0, bgColorTop,
                0, Game. GAME_HEIGHT, bgColorBottom
        );
        g.setPaint(gradient);
        g.fillRect(0, 0, Game. GAME_WIDTH, Game. GAME_HEIGHT);
    }


    private void drawTitle(Graphics2D g) {
        g.setFont(titleFont);
        FontMetrics metrics = g.getFontMetrics(titleFont);
        int titleX = (Game.GAME_WIDTH - metrics.stringWidth(title)) / 2;

        // Shadow
        g.setColor(titleShadow);
        g.drawString(title, titleX + 3, titleY + 3);

        // Main title
        g.setColor(titleColor);
        g.drawString(title, titleX, titleY);

        // Underline effect
        g.setColor(titleUnderline);
        g.setStroke(new BasicStroke(3));
        g.drawLine(titleX, titleY + 10,
                titleX + metrics. stringWidth(title), titleY + 10);
    }


    private void drawOptions(Graphics2D g, int currentSelection, float animationOffset) {
        g.setFont(optionFont);
        FontMetrics metrics = g.getFontMetrics(optionFont);

        for (int i = 0; i < options.length; i++) {
            int optionX = (Game. GAME_WIDTH - metrics.stringWidth(options[i])) / 2;
            int optionY = optionsStartY + i * optionsSpacing;

            if (i == currentSelection) {
                drawSelectedOption(g, metrics, options[i], optionX, optionY, animationOffset);
            } else {
                drawNormalOption(g, options[i], optionX, optionY);
            }
        }
    }


    private void drawSelectedOption(Graphics2D g, FontMetrics metrics,
                                    String option, int x, int y, float animationOffset) {
        // Highlight box
        int boxPadding = 20;
        int boxWidth = metrics.stringWidth(option) + boxPadding * 2;
        int boxHeight = metrics.getHeight();
        int boxX = x - boxPadding;
        int boxY = y - metrics.getAscent();

        // Box background
        g.setColor(selectedBg);
        g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Box border
        g.setColor(selectedBorder);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Animated arrow
        int arrowOffset = (int)(Math.sin(animationOffset) * 5);
        drawArrow(g, x - 50 + arrowOffset, y - 8);

        // Selected text
        g.setColor(selectedText);
        g.drawString(option, x, y);
    }


    private void drawNormalOption(Graphics2D g, String option, int x, int y) {
        g.setColor(normalText);
        g.drawString(option, x, y);
    }


    private void drawArrow(Graphics2D g, int x, int y) {
        int[] xPoints = {x, x + 15, x};
        int[] yPoints = {y, y + 8, y + 16};

        // Fill
        g.setColor(arrowColor);
        g.fillPolygon(xPoints, yPoints, 3);

        // Border
        g.setColor(arrowBorder);
        g.setStroke(new BasicStroke(2));
        g.drawPolygon(xPoints, yPoints, 3);
    }


    private void drawHint(Graphics2D g) {
        g.setFont(hintFont);
        String hint = "↑↓ Navigate  •  ENTER Select";
        FontMetrics metrics = g.getFontMetrics(hintFont);
        int hintX = (Game.GAME_WIDTH - metrics.stringWidth(hint)) / 2;

        g.setColor(hintColor);
        g.drawString(hint, hintX, Game.GAME_HEIGHT - 40);
    }
}