package entity.player.components;

import entity.ICollidable;
import entity. player.Player;
import entity.player.PlayerPhysics;
import java.util. List;

/**
 * Component quản lý physics của Player.
 *
 * SOLID:
 * - SRP:  Chỉ lo physics (gravity, movement, collision)
 * - DIP: Nhận List<ICollidable> thay vì ArrayList<Enemy>
 *
 * Wrap PlayerPhysics class cũ để tái sử dụng logic
 */
public class PlayerPhysicsComponent implements PlayerComponent {

    private final PlayerPhysics physics;

    /**
     * Constructor
     * @param player Entity để PlayerPhysics theo dõi hitbox
     */
    public PlayerPhysicsComponent(Player player) {
        this.physics = new PlayerPhysics(player);
    }

    /**
     * Update physics mỗi frame
     * - Đọc input từ Player
     * - Gọi physics.update() với level data và input flags
     */
    @Override
    public void update(Player player, float deltaTime) {
        // Lấy input state từ InputHandler trong Player
        boolean left = player.getInputHandler().isLeft();
        boolean right = player.getInputHandler().isRight();
        boolean jump = player.getInputHandler().isJump();

        // Lấy level data để check collision với tiles
        int[][] lvlData = player.getLevelData();

        // Update physics (gravity, movement, tile collision)
        physics.update(lvlData, left, right, jump);
    }

    /**
     * Reset physics state (khi respawn)
     */
    @Override
    public void reset() {
        physics.resetInAir();
    }

    // ===== Getters cho các component khác =====

    public PlayerPhysics getPhysics() {
        return physics;
    }

    public boolean isInAir() {
        return physics.isInAir();
    }

    public float getAirSpeed() {
        return physics. getAirSpeed();
    }

    /**
     * Set obstacles để check collision
     * Dùng ICollidable interface (DIP)
     */
    public void setObstacles(List<ICollidable> obstacles) {
        physics.setObstacles(obstacles);
    }
}