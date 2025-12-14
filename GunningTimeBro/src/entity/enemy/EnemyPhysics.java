package entity.enemy;

import main.Game;
import utilz.PhysicsHelper;
import static utilz.Constants.EnemyConstants.*;  // ✅ USE CONSTANTS
import java.awt.geom.Rectangle2D;

public class EnemyPhysics {

    private Enemy enemy;
    private float walkSpeed = 1.0f * Game.SCALE;
    private float gravity = 0.04f * Game.SCALE;
    private float airSpeed = 0f;
    private boolean inAir = true;

    public EnemyPhysics(Enemy enemy) {
        this.enemy = enemy;
    }

    public void update(int[][] lvlData) {
        int state = enemy.getEnemyState();
        if (state == ATTACK || state == DEAD || state == IDLE) return;

        updatePos(lvlData);
    }

    private void updatePos(int[][] lvlData) {
        Rectangle2D.Float hitbox = enemy.getHitbox();
        boolean flip = enemy.isFlip();
        int tileSize = Game.TILES_SIZE;

        if (! inAir && ! PhysicsHelper.isEntityOnFloor(hitbox, lvlData, tileSize)) {
            inAir = true;
        }

        if (inAir) {
            if (PhysicsHelper.canMoveHere(hitbox. x, hitbox.y + airSpeed,
                    hitbox.width, hitbox.height, lvlData, tileSize)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
            } else {
                hitbox.y = PhysicsHelper.getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed, tileSize);
                inAir = airSpeed <= 0;
                airSpeed = 0;
            }
        }

        float xSpeed = flip ? -walkSpeed : walkSpeed;
        float newX = hitbox.x + xSpeed;

        if (PhysicsHelper.canMoveHere(newX, hitbox.y, hitbox.width, hitbox. height, lvlData, tileSize)) {
            if (PhysicsHelper.isFloor(hitbox, xSpeed, lvlData, tileSize)) {
                hitbox.x = newX;
            } else {
                enemy.setFlip(! flip);
            }
        } else {
            hitbox.x = PhysicsHelper.getEntityXPosNextToWall(hitbox, xSpeed, tileSize);
            enemy.setFlip(!flip);
        }
    }

    public void resetInAir() {
        inAir = true;
        airSpeed = 0;
    }

    public boolean isInAir() { return inAir; }
    public void setInAir(boolean inAir) { this.inAir = inAir; }
}