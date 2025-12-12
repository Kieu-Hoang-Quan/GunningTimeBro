package entity.enemy;

import main.Game;
import java.awt.geom.Rectangle2D;
import static utilz.HelpMethods.*;
import static utilz.Constants.EnemyConstants.*;

public class EnemyPhysics {

    private Enemies enemy;

    // Các chỉ số vật lý
    private float walkSpeed = 1.0f * Game.SCALE;
    private float gravity = 0.04f * Game.SCALE;
    private float airSpeed = 0f;
    private boolean inAir = true;

    public EnemyPhysics(Enemies enemy) {
        this.enemy = enemy;
    }

    public void update(int[][] lvlData) {
        // Chặn di chuyển nếu đang Đánh, Chết hoặc Đứng yên (IDLE)
        int state = enemy.getEnemyState();
        if (state == ATTACK || state == DEAD || state == IDLE) return;

        updatePos(lvlData);
    }

    private void updatePos(int[][] lvlData) {
        Rectangle2D.Float hitbox = enemy.getHitbox();
        boolean flip = enemy.isFlip();

        // 1. Xử lý Trọng lực (Y)
        if (!inAir && !IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) { // Chạm đất
                    inAir = false;
                    airSpeed = 0;
                } else { // Đụng trần
                    airSpeed = 0;
                }
            }
        }

        // 2. Xử lý Di chuyển ngang (X)
        float xSpeed = flip ? -walkSpeed : walkSpeed;
        float newX = hitbox.x + xSpeed;

        if (CanMoveHere(newX, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x = newX;
            } else {
                // Hết đường (Vực) -> Quay đầu
                enemy.setFlip(!flip);
            }
        } else {
            // Đụng tường -> Quay đầu
            // Tinh chỉnh nhẹ vị trí sát tường để không kẹt
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            enemy.setFlip(!flip);
        }
    }

    // Hỗ trợ reset khi spawn
    public void resetInAir() {
        inAir = true;
        airSpeed = 0;
    }

    public boolean isInAir() { return inAir; }
    public void setInAir(boolean inAir) { this.inAir = inAir; }
}