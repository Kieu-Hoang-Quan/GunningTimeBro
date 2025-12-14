package entity.enemy;

import static utilz.Constants.EnemyConstants.*;

public class EnemyAnimator {

    private Enemy enemy;
    private int aniIndex, aniTick;
    private int walkAniSpeed = 15;
    private int attackAniSpeed = 28;
    private int holdAttackFrameTicks = 7;
    private int holdTick = 0;

    public EnemyAnimator(Enemy enemy) {
        this.enemy = enemy;
    }

    public void update() {
        int state = enemy.getEnemyState();
        int enemyType = enemy.getEnemyType();
        int speed = (state == ATTACK) ? attackAniSpeed : walkAniSpeed;

        aniTick++;
        if (aniTick >= speed) {
            aniTick = 0;
            aniIndex++;

            int maxFrames = GetSpriteAmount(enemyType, state);

            if (aniIndex >= maxFrames) {
                if (state == ATTACK) {
                    aniIndex = maxFrames - 1;
                    holdTick++;
                    if (holdTick >= holdAttackFrameTicks) {
                        holdTick = 0;
                        enemy.setAttackChecked(false);
                        enemy.setNewState(RUNNING);
                    }
                } else if (state == DEAD) {
                    aniIndex = maxFrames - 1;
                    enemy.setActive(false);
                } else {
                    aniIndex = 0;
                }
            }
        }
    }

    public void reset() {
        aniTick = 0;
        aniIndex = 0;
        holdTick = 0;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}