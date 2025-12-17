package entity.player.components;

import entity.player.Player;

/**
 * Base interface cho tất cả Player components.
 *
 * SOLID: Interface Segregation Principle
 * - Interface nhỏ gọn, chỉ có 2 method cần thiết
 * - Mỗi component implement riêng logic của mình
 *
 * Lifecycle:
 * - update(): gọi mỗi frame trong game loop
 * - reset(): gọi khi restart game
 */
public interface PlayerComponent {

    /**
     * Update logic của component mỗi frame
     *
     * @param player Reference đến Player để truy cập data
     * @param deltaTime Thời gian giữa 2 frame (seconds)
     */
    void update(Player player, float deltaTime);

    /**
     * Reset state nội bộ về mặc định
     * Gọi khi restart game hoặc respawn
     */
    void reset();
}