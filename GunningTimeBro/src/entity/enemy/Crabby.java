package entity.enemy;

import entity.player.Player;
import main.Game;
import static utilz.Constants.EnemyConstants.*;

public class Crabby extends Enemies {

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);

        // 1. Cấu hình Hitbox (Cho Punk - Cao hơn)
        initHitbox(x, y, (int)(22 * Game.SCALE), (int)(30 * Game.SCALE));

        // 2. Cấu hình Offset vẽ (Để chân chạm đất)
        this.xDrawOffset = (int) (9 * Game.SCALE);
        this.yDrawOffset = (int) (10 * Game.SCALE);

        // 3. Chỉnh chiều cao vùng tấn công
        attackBox.height = (int)(30 * Game.SCALE);
    }
}