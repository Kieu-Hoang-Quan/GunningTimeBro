package entity.enemy.systems;

import entity.enemy.Enemy;
import entity.player.Player;
import java.util.List;

/**
 * System cập nhật trạng thái enemies.
 * SOLID: SRP - Chỉ lo update
 */
public class EnemyUpdateSystem {

    public void update(List<Enemy> enemies, int[][] lvlData, Player player) {
        for (Enemy enemy :  enemies) {
            if (enemy.isActive()) {
                enemy.update(lvlData, player);
            }
        }
    }
}