package entity. enemy.systems;

import entity. enemy.Enemy;
import java.util.List;

/**
 * System cleanup inactive enemies.
 * SOLID:  SRP - Chỉ lo cleanup
 */
public class EnemyCleanupSystem {

    public void cleanup(List<Enemy> enemies) {
        enemies.removeIf(e -> !e.isActive());
    }
}