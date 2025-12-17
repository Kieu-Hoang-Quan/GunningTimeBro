package entity;

import java.awt. geom.Rectangle2D;

/**
 * Interface cho bất kỳ entity nào có thể va chạm.
 *
 * SOLID:  Dependency Inversion Principle
 * - PlayerPhysics sẽ phụ thuộc interface này thay vì Enemy cụ thể
 * - Dễ mock để unit test
 * - Dễ mở rộng (thêm obstacle, NPC, etc.)
 */
public interface ICollidable {

    /**
     * Lấy hitbox để kiểm tra va chạm
     */
    Rectangle2D. Float getHitbox();

    /**
     * Kiểm tra entity có đang active không
     * (inactive = không cần check collision)
     */
    boolean isActive();
}