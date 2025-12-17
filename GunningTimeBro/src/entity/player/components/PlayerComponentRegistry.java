package entity.player.components;

import entity.player.Player;

import java.util.*;

/**
 * Quản lý vòng đời và truy xuất tất cả Player components.
 *
 * SOLID: Single Responsibility Principle
 * - Chỉ lo việc quản lý components
 * - Không chứa logic gameplay
 *
 * Design Pattern:  Registry Pattern
 * - Centralized storage cho components
 * - Type-safe access qua generic method
 */
public class PlayerComponentRegistry {

    /** Map lưu component theo Class type để get nhanh */
    private final Map<Class<? >, PlayerComponent> components = new HashMap<>();

    /** List giữ thứ tự update (Physics → Combat → Animation) */
    private final List<PlayerComponent> updateOrder = new ArrayList<>();

    /**
     * Đăng ký component mới
     *
     * @param type Class type để làm key (vd: PlayerPhysicsComponent. class)
     * @param component Instance của component
     */
    public <T extends PlayerComponent> void register(Class<T> type, T component) {
        components.put(type, component);
        updateOrder.add(component);
    }

    /**
     * Lấy component theo type
     *
     * @param type Class type cần lấy
     * @return Component instance hoặc null nếu không có
     *
     * Ví dụ:  registry.get(PlayerCombatComponent.class)
     */
    @SuppressWarnings("unchecked")
    public <T extends PlayerComponent> T get(Class<T> type) {
        return (T) components.get(type);
    }

    /**
     * Update tất cả components theo thứ tự đăng ký
     * Thứ tự quan trọng: Physics trước để có vị trí mới,
     * Combat sau để dùng vị trí đó, Animation cuối cùng
     */
    public void updateAll(Player player, float deltaTime) {
        for (PlayerComponent component : updateOrder) {
            component.update(player, deltaTime);
        }
    }

    /**
     * Reset tất cả components
     * Gọi khi restart game
     */
    public void resetAll() {
        for (PlayerComponent component : updateOrder) {
            component.reset();
        }
    }

    /**
     * Kiểm tra có component type này không
     */
    public boolean has(Class<?> type) {
        return components.containsKey(type);
    }
}