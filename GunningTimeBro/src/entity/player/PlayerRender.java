package entity.player;

import main.Game;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PlayerRender {
    // Offset vẽ hình (căn chỉnh hitbox với hình ảnh)
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 25 * Game.SCALE;

    public void render(Graphics g, Player player, int camX) {
        BufferedImage currentFrame = player.getAnimator().getCurrentFrame();
        Rectangle2D.Float hitbox = player.getHitbox();

        int spriteW = player.getWidth();
        int spriteH = player.getHeight();

        int drawY = (int) (hitbox.y - yDrawOffset);

        if (player.isFlip()) {
            int drawXWorld = (int) (hitbox.x + hitbox.width + xDrawOffset);
            int screenX = drawXWorld - camX;

            // Vẽ lật ngược (flip)
            g.drawImage(currentFrame, screenX, drawY, -spriteW, spriteH, null);
        } else {
            int drawXWorld = (int) (hitbox.x - xDrawOffset);
            int screenX = drawXWorld - camX;

            // Vẽ bình thường
            g.drawImage(currentFrame, screenX, drawY, spriteW, spriteH, null);
        }

    }
}