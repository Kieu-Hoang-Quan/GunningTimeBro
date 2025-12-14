package entity.player;

import entity.Entity;
import entity.enemy.Enemy;
import main.Game;
import utilz.PhysicsHelper;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class PlayerPhysics {

    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private float playerSpeed = 1.5f * Game.SCALE;
    private boolean inAir = false;

    private Entity entity;
    private ArrayList<Enemy> enemies;

    public PlayerPhysics(Entity entity) {
        this.entity = entity;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void update(int[][] lvlData, boolean left, boolean right, boolean jump) {
        Rectangle2D.Float hitbox = entity.getHitbox();
        int tileSize = Game.TILES_SIZE;
        float xSpeed = 0;

        if (jump) {
            jump();
        }

        if (! left && !right && !inAir) {
            return;
        }

        if (left) xSpeed -= playerSpeed;
        if (right) xSpeed += playerSpeed;

        if (! inAir) {
            if (! PhysicsHelper.isEntityOnFloor(hitbox, lvlData, tileSize)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (PhysicsHelper.canMoveHere(hitbox. x, hitbox.y + airSpeed,
                    hitbox.width, hitbox.height, lvlData, tileSize)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed, lvlData, tileSize);
            } else {
                hitbox.y = PhysicsHelper.getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed, tileSize);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed, lvlData, tileSize);
            }
        } else {
            updateXPos(xSpeed, lvlData, tileSize);
        }
    }

    private void updateXPos(float xSpeed, int[][] lvlData, int tileSize) {
        Rectangle2D.Float hitbox = entity.getHitbox();

        // CHECK TILE COLLISION
        if (! PhysicsHelper.canMoveHere(hitbox. x + xSpeed, hitbox.y,
                hitbox.width, hitbox.height, lvlData, tileSize)) {
            hitbox.x = PhysicsHelper.getEntityXPosNextToWall(hitbox, xSpeed, tileSize);
            return;
        }

        // CHECK ENEMY COLLISION
        if (enemies != null) {
            Rectangle2D.Float testBox = new Rectangle2D.Float(
                    hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox. height
            );

            for (Enemy enemy : enemies) {
                if (enemy.isActive() && enemy.getHealthComponent().isAlive()) {
                    if (testBox.intersects(enemy.getHitbox())) {
                        return;  // Block movement
                    }
                }
            }
        }

        // NO COLLISION - MOVE
        hitbox.x += xSpeed;
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