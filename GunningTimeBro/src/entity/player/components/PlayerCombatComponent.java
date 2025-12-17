package entity.player.components;

import entity.ICollidable;
import entity.player.Player;
import entity.components.MeleeComponent;
import entity. components. HealthComponent;
import main.Game;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Component quản lý combat của Player.
 *
 * SOLID:
 * - SRP:  Chỉ lo combat (attack, damage, combo, lightning)
 * - DIP: Target là ICollidable, không phải Enemy cụ thể
 *
 * Responsibilities:
 * - Quản lý attack state machine
 * - Tính damage với buffs
 * - Check hit collision
 * - Handle lightning attack
 */
public class PlayerCombatComponent implements PlayerComponent {

    // ===== Dependencies =====
    private final MeleeComponent melee;
    private final Rectangle2D.Float attackBox;

    // ===== Attack State =====
    private boolean attacking = false;
    private boolean attackStarted = false;
    private boolean attackHit = false;
    private boolean usingLightning = false;

    // ===== Targets (DIP:  interface, không phải concrete class) =====
    private List<ICollidable> targets;

    // ===== Constants =====
    private static final float NORMAL_RANGE = 20f * Game.SCALE;
    private static final float LIGHTNING_RANGE = 40f * Game.SCALE;
    private static final int HIT_FRAME = 3; // Frame gây damage

    /**
     * Constructor
     * @param player Owner của component
     * @param baseDamage Base damage cho MeleeComponent
     */
    public PlayerCombatComponent(Player player, float baseDamage) {
        this.melee = new MeleeComponent(player, baseDamage);
        this.attackBox = new Rectangle2D.Float(0, 0, NORMAL_RANGE, 15 * Game.SCALE);
    }

    /**
     * Update combat mỗi frame
     *
     * Flow:
     * 1. Update melee cooldown/combo decay
     * 2. Apply damage buffs từ BuffSystem
     * 3. Update attack state machine
     * 4. Update attack box position
     */
    @Override
    public void update(Player player, float deltaTime) {
        // 1. Update melee internals (cooldown, combo decay)
        melee.update(deltaTime);

        // 2. Apply damage buff từ BuffSystem
        float damageBoost = player.getBuffSystem().getTotalBuffValue("DAMAGE_BOOST");
        melee.setDamageMultiplier(1.0f + damageBoost);

        // 3. Update attack state machine
        updateAttackState(player);

        // 4. Update attack box theo hướng player
        updateAttackBox(player);
    }

    /**
     * Attack State Machine
     *
     * States:
     * - IDLE: attackStarted = false
     * - ATTACKING: attackStarted = true, đang chạy animation
     * - HIT_FRAME: animation tới frame 3 → check damage
     * - FINISHED: animation xong → reset về IDLE
     */
    private void updateAttackState(Player player) {
        if (!attackStarted) return;

        attacking = true;

        // Check hit tại frame 3 (chỉ 1 lần per attack)
        int currentFrame = player.getAnimator().getAniIndex();
        if (currentFrame == HIT_FRAME && !attackHit && targets != null) {
            performAttackHit(player);
            attackHit = true;
        }

        // Kết thúc khi animation xong
        if (player.getAnimator().isAnimationFinished()) {
            endAttack(player);
        }
    }

    /**
     * Thực hiện đòn đánh - check collision và gây damage
     */
    private void performAttackHit(Player player) {
        // Tính damage dựa trên attack type
        int attackType = player.getAnimationState().getCurrentAction();
        float damage = melee.calculateDamage(attackType);

        // Check collision với từng target
        for (ICollidable target : targets) {
            if (! target.isActive()) continue;

            if (attackBox.intersects(target.getHitbox())) {
                // Gây damage nếu target có HealthComponent
                applyDamageToTarget(target, damage, player);
                melee.incrementCombo();
            }
        }
    }

    /**
     * Gây damage cho target
     * Sử dụng reflection để không phụ thuộc concrete class
     */
    private void applyDamageToTarget(ICollidable target, float damage, Player player) {
        try {
            // Lấy HealthComponent qua reflection
            var method = target.getClass().getMethod("getHealthComponent");
            var healthComp = (HealthComponent) method.invoke(target);
            if (healthComp != null && healthComp.isAlive()) {
                healthComp.takeDamage(damage, player);
            }
        } catch (Exception e) {
            // Target không có HealthComponent - bỏ qua
        }
    }

    /**
     * Update vị trí attack box theo hướng player
     */
    private void updateAttackBox(Player player) {
        // Range phụ thuộc vào loại attack
        float range = usingLightning ? LIGHTNING_RANGE : NORMAL_RANGE;
        attackBox.width = range;

        // Position theo hướng flip
        Rectangle2D.Float hitbox = player.getHitbox();
        if (player.isFlip()) {
            // Facing left: attack box bên trái
            attackBox.x = hitbox.x - attackBox.width;
        } else {
            // Facing right:  attack box bên phải
            attackBox.x = hitbox. x + hitbox.width;
        }
        attackBox.y = hitbox.y;
    }

    /**
     * Bắt đầu attack thường
     * Gọi từ Player.setAttacking(true)
     */
    public void startAttack(Player player) {
        if (attacking || attackStarted) return; // Đang attack rồi

        attackStarted = true;
        attackHit = false;
        usingLightning = false;
        player.getAnimator().resetAnimation();
    }

    /**
     * Bắt đầu lightning attack
     * - Kiểm tra và trừ charge
     * - Set flag usingLightning để dùng range/damage khác
     */
    public void startLightningAttack(Player player) {
        if (attacking || attackStarted) return;

        // Kiểm tra và trừ charge
        if (! player.getAbilitySystem().useLightning()) {
            return; // Không đủ charge
        }

        usingLightning = true;
        attackStarted = true;
        attackHit = false;
        player.getAnimator().resetAnimation();
    }

    /**
     * Kết thúc attack - reset tất cả flags
     */
    private void endAttack(Player player) {
        attacking = false;
        attackStarted = false;
        attackHit = false;
        usingLightning = false;
        player.getAnimator().resetAnimation();
    }

    @Override
    public void reset() {
        attacking = false;
        attackStarted = false;
        attackHit = false;
        usingLightning = false;
        melee.resetCombo();
    }

    // ===== Getters/Setters =====

    public void setTargets(List<ICollidable> targets) {
        this.targets = targets;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean isUsingLightning() {
        return usingLightning;
    }

    public MeleeComponent getMeleeComponent() {
        return melee;
    }
}