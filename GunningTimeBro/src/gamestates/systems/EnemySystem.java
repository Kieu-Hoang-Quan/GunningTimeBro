package gamestates.systems;

import entity.enemy.EnemyManager;

public class EnemySystem {
    private final EnemyManager enemyManager;

    public EnemySystem(EnemyManager enemyManager) { this.enemyManager = enemyManager; }
    public void update() { enemyManager.update(); }
}