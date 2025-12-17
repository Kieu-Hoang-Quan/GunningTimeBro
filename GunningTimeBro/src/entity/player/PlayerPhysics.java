package entity.player;

import entity.Entity;
import entity.ICollidable;
import main.Game;
import utilz. PhysicsHelper;

import java.awt. geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Player Physics - Movement và Collision.
 *
 * SOLID:
 * - SRP: Chỉ lo physics
 * - DIP: Dùng ICollidable interface thay vì Enemy cụ thể
 */
public class PlayerPhysics {

    // ===== Physics Constants =====
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private float playerSpeed = 1.5f * Game.SCALE;

    // ===== State =====
    private float airSpeed = 0f;
    private boolean inAir = false;

    // ===== References =====
    private Entity entity;

    // ===== Obstacles (DIP:  interface thay vì concrete class) =====
    private List<ICollidable> obstacles = new ArrayList<>();

    public PlayerPhysics(Entity entity) {
        this.entity = entity;
    }

    /**
     * Set obstacles để check collision
     * @param obstacles List của ICollidable (Enemy, NPC, etc.)
     */
    public void setObstacles(List<ICollidable> obstacles) {
        this.obstacles = obstacles != null ? obstacles : new ArrayList<>();
    }

    // Backward compatibility
    public void setEnemies(ArrayList<? > enemies) {
        if (enemies != null && ! enemies.isEmpty() && enemies.get(0) instanceof ICollidable) {
            this.obstacles = new ArrayList<>();
            for (Object obj : enemies) {
                this.obstacles.add((ICollidable) obj);
            }
        }
    }

    /**
     * Update physics mỗi frame
     */
    public void update(int[][] lvlData, boolean left, boolean right, boolean jump) {
        Rectangle2D.Float hitbox = entity.getHitbox();
        int tileSize = Game.TILES_SIZE;
        float xSpeed = 0;

        // Handle jump
        if (jump) {
            tryJump();
        }

        // Early return nếu không có input và đang trên đất
        if (!left && !right && !inAir) {
            return;
        }

        // Calculate horizontal speed
        if (left) xSpeed -= playerSpeed;
        if (right) xSpeed += playerSpeed;

        // Check nếu rơi khỏi platform
        if (! inAir && ! PhysicsHelper.isEntityOnFloor(hitbox, lvlData, tileSize)) {
            inAir = true;
        }

        // Update position
        if (inAir) {
            updateInAir(hitbox, lvlData, tileSize, xSpeed);
        } else {
            updateXPos(xSpeed, lvlData, tileSize);
        }
    }

    /**
     * Update khi đang trong không trung
     */
    private void updateInAir(Rectangle2D. Float hitbox, int[][] lvlData,
                             int tileSize, float xSpeed) {
        // Try move vertically
        if (PhysicsHelper. canMoveHere(hitbox. x, hitbox.y + airSpeed,
                hitbox.width, hitbox.height, lvlData, tileSize)) {
            hitbox.y += airSpeed;
            airSpeed += gravity;
            updateXPos(xSpeed, lvlData, tileSize);
        } else {
            // Collided vertically
            hitbox.y = PhysicsHelper.getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed, tileSize);
            if (airSpeed > 0) {
                // Landed
                resetInAir();
            } else {
                // Hit ceiling
                airSpeed = fallSpeedAfterCollision;
            }
            updateXPos(xSpeed, lvlData, tileSize);
        }
    }

    /**
     * Update horizontal position với collision check
     */
    private void updateXPos(float xSpeed, int[][] lvlData, int tileSize) {
        Rectangle2D.Float hitbox = entity.getHitbox();

        // Check tile collision
        if (! PhysicsHelper.canMoveHere(hitbox. x + xSpeed, hitbox.y,
                hitbox.width, hitbox.height, lvlData, tileSize)) {
            hitbox.x = PhysicsHelper.getEntityXPosNextToWall(hitbox, xSpeed, tileSize);
            return;
        }

        // Check obstacle collision (DIP: dùng ICollidable)
        Rectangle2D.Float testBox = new Rectangle2D.Float(
                hitbox.x + xSpeed, hitbox.y, hitbox. width, hitbox.height
        );

        for (ICollidable obstacle : obstacles) {
            if (obstacle.isActive() && testBox.intersects(obstacle.getHitbox())) {
                return; // Block movement
            }
        }

        // No collision - move
        hitbox.x += xSpeed;
    }

    /**
     * Try to jump
     */
    private void tryJump() {
        if (inAir) return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    public void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    // ===== Getters/Setters =====

    public boolean isInAir() { return inAir; }
    public void setInAir(boolean inAir) { this.inAir = inAir; }
    public float getAirSpeed() { return airSpeed; }
}