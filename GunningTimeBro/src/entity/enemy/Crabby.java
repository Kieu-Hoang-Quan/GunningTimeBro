package entity.enemy;

import main.Game;
import entity.player.Player;
import static utilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy {

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);

        // 1. Cấu hình Hitbox
        initHitbox(x, y, (int)(22 * Game.SCALE), (int)(30 * Game.SCALE));

        // 2. Cấu hình Offset vẽ
        this.xDrawOffset = (int) (9 * Game.SCALE);
        this.yDrawOffset = (int) (10 * Game.SCALE);

        // 3. Chỉnh chiều cao vùng tấn công
        attackBox.height = (int)(30 * Game.SCALE);
    }

    // ✅ OVERRIDE logic hành vi riêng cho Crabby
    @Override
    public void updateBehavior(int[][] lvlData, Player player) {
        if (enemyState == IDLE) {
            // Crabby mặc định là chạy, không đứng yên lâu
            setNewState(RUNNING);
        }

        if (enemyState == HIT) {
            if (animator.getAniIndex() >= GetSpriteAmount(enemyType, HIT) - 1)
                setNewState(RUNNING);
            return;
        }

        // Logic cũ: Tính khoảng cách tới người chơi
        float px = player.getHitbox().x;
        float ex = hitbox.x;
        float distX = Math.abs(px - ex);
        float distY = Math.abs(player.getHitbox().y - hitbox.y);
        boolean sameLevel = distY < hitbox.height * 0.6f;

        // Logic tấn công
        if (sameLevel && distX < attackTriggerRange) {
            flip = px < ex;
            setNewState(ATTACK);
            return;
        }

        // Logic đuổi theo
        if (sameLevel && distX < chaseRange) {
            flip = px < ex;
            setNewState(RUNNING);
            return;
        }

        // Nếu không làm gì thì cứ chạy tuần tra
        setNewState(RUNNING);
    }
}