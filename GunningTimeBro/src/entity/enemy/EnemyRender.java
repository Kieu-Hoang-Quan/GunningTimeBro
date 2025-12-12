package entity.enemy;

import main.Game;
import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.EnemyConstants.*;

public class EnemyRender {

    public void render(Graphics g, Enemies enemy, BufferedImage[][] sprites, int xOff) {
        if (!enemy.isActive()) return;

        int state = enemy.getEnemyState();
        int frame = enemy.getAnimator().getAniIndex();

        // Kiểm tra an toàn để tránh lỗi Crash
        if (state >= sprites.length || frame >= sprites[state].length) return;

        // Tính vị trí vẽ
        int tempX = (int) (enemy.getHitbox().x - enemy.getXDrawOffset() - xOff);
        int tempY = (int) (enemy.getHitbox().y - enemy.getYDrawOffset());

        int W = CRABBY_WIDTH;
        int H = CRABBY_HEIGHT;

        if (enemy.isFlip()) {
            // Logic vẽ lật hình (Flip) chuẩn xác
            // Công thức: tempX + Width + (Offset cân bằng)
            int flipX = (int)(2 * enemy.getXDrawOffset() + enemy.getHitbox().width - W);

            g.drawImage(sprites[state][frame],
                    tempX + W + flipX, tempY, -W, H, null);
        } else {
            // Vẽ bình thường
            g.drawImage(sprites[state][frame],
                    tempX, tempY, W, H, null);
        }

        // Uncomment dòng dưới nếu muốn xem khung va chạm
        // enemy.drawDebug(g);
    }
}