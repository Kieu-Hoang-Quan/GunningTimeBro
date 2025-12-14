package entity.enemy;

import static utilz.Constants.EnemyConstants.*;

public class EnemyFactory {

    /**
     * Factory Method: Nơi duy nhất quyết định tạo ra con quái nào.
     * Khi muốn thêm quái mới (VD: Shark), chỉ cần thêm case vào đây.
     */
    public static Enemy createEnemy(int enemyType, float x, float y) {
        switch (enemyType) {
            case CRABBY:
                return new Crabby(x, y);
            // Sau này thêm: case SHARK: return new Shark(x, y);
            default:
                return null;
        }
    }
}