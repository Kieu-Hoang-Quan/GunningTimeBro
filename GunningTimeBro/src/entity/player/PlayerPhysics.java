package entity.player;

import entity.Entity;
import main.Game;
import java.awt.geom.Rectangle2D;
import static utilz.HelpMethods.*;

public class PlayerPhysics {

    // Physics constants
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private float playerSpeed = 1.5f * Game.SCALE;
    private boolean inAir = false;

    // References
    private Entity entity;

    public PlayerPhysics(Entity entity) {
        this.entity = entity;
    }

    public void update(int[][] lvlData, boolean left, boolean right, boolean jump) {
        Rectangle2D.Float hitbox = entity.getHitbox();
        float xSpeed = 0;

        // 1. Check Jump state
        if (jump) {
            jump();
        }

        // 2. Check Input Movement
        if (!left && !right && !inAir) {
            return;
        }

        if (left) xSpeed -= playerSpeed;
        if (right) xSpeed += playerSpeed;

        // 3. Check Ground
        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        // 4. Apply Movement & Gravity
        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed, lvlData);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed, lvlData);
            }
        } else {
            updateXPos(xSpeed, lvlData);
        }
    }

    private void updateXPos(float xSpeed, int[][] lvlData) {
        Rectangle2D.Float hitbox = entity.getHitbox();
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void jump() {
        if (inAir) return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    public void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public boolean isInAir() {
        return inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public float getAirSpeed() {
        return airSpeed;
    }
}