package entity.enemy.systems;

import entity.enemy.Enemy;
import entity.player.Player;
import java.util.List;
import static utilz.Constants.EnemyConstants. ATTACK;

/**
 * System xử lý combat enemies.
 * SOLID: SRP - Chỉ lo combat
 */
public class EnemyCombatSystem {

    public void handleAttacks(List<Enemy> enemies, Player player) {
        for (Enemy enemy : enemies) {
            if (enemy.getEnemyState() != ATTACK) continue;
            if (enemy.attackChecked) continue;
            if (! enemy.isAttackFrame()) continue;

            if (enemy.getAttackBox().intersects(player.getHitbox())) {
                player.getHealthComponent().takeDamage(enemy.attackDamage, enemy);
            }
            enemy.attackChecked = true;
        }
    }
}