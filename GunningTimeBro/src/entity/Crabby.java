package entity;

import utilz.Constants.EnemyConstants;
import static utilz.Constants.EnemyConstants.*;  // ✔ FIX IMPORT

public class Crabby extends Enemies {

    public Crabby(float x, float y) {
        super(x, y,
                EnemyConstants.CRABBY_WIDTH,
                EnemyConstants.CRABBY_HEIGHT,
                EnemyConstants.CRABBY
        );
    }

    @Override
    public void update(int[][] lvlData) {
        super.update(lvlData);
        // (nếu cần thêm behavior riêng thì thêm vào đây)
    }
}
