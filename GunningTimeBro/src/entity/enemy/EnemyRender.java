package entity.enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.EnemyConstants.*;

public class EnemyRender {

    public void render(Graphics g, Enemy enemy, BufferedImage[][] sprites, int xOff) {
        if (! enemy.isActive()) return;

        int state = enemy.getEnemyState();
        int frame = enemy.getAnimator().getAniIndex();

        if (state >= sprites.length || frame >= sprites[state].length) return;

        int tempX = (int) (enemy.getHitbox().x - enemy.getXDrawOffset() - xOff);
        int tempY = (int) (enemy.getHitbox().y - enemy.getYDrawOffset());

        int W = CRABBY_WIDTH;
        int H = CRABBY_HEIGHT;

        BufferedImage img = sprites[state][frame];

        // FADE EFFECT WHEN DEAD
        if (state == DEAD) {
            Graphics2D g2d = (Graphics2D) g;
            float alpha = 1.0f - (enemy.getDeathTimer() / 60f);  // Fade over 60 frames
            if (alpha < 0) alpha = 0;

            AlphaComposite ac = AlphaComposite. getInstance(AlphaComposite.SRC_OVER, alpha);
            g2d.setComposite(ac);
        }

        if (enemy.isFlip()) {
            int flipX = (int)(2 * enemy.getXDrawOffset() + enemy.getHitbox().width - W);
            g.drawImage(img, tempX + W + flipX, tempY, -W, H, null);
        } else {
            g.drawImage(img, tempX, tempY, W, H, null);
        }

        // RESET ALPHA
        if (state == DEAD) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite. SRC_OVER, 1.0f));
        }
    }
}