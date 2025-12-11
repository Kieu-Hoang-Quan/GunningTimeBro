package entity.enemy;

import static utilz.Constants.EnemyConstants.*;

public class EnemyAnimator {

    private Enemies enemy;
    private int aniIndex, aniTick;

    // Tốc độ animation
    private int walkAniSpeed = 15;
    private int attackAniSpeed = 28;

    // Logic giữ frame đánh
    private int holdAttackFrameTicks = 7;
    private int holdTick = 0;

    public EnemyAnimator(Enemies enemy) {
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

            if (state == ATTACK) {
                // Logic giữ frame cuối khi đánh
                if (aniIndex >= maxFrames) {
                    aniIndex = maxFrames - 1;
                    holdTick++;
                    if (holdTick >= holdAttackFrameTicks) {
                        holdTick = 0;
                        enemy.setAttackChecked(false);
                        enemy.setNewState(RUNNING); // Đánh xong quay về chạy
                    }
                    return;
                }
            }
            else if (state == DEAD) {
                // Logic chết xong thì active = false
                if (aniIndex >= maxFrames) {
                    aniIndex = maxFrames - 1;
                    enemy.setActive(false);
                }
            }
            else {
                // Loop các trạng thái khác (Idle, Run, Hit)
                if (aniIndex >= maxFrames) aniIndex = 0;
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