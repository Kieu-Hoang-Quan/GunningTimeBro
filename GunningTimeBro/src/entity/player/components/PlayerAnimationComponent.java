package entity.player.components;

import entity.player.Player;
import entity.player.PlayerAnimator;

/**
 * Component quản lý animation của Player.
 *
 * SOLID:
 * - SRP: Chỉ lo animation (không physics, không combat)
 *
 * Dependencies:
 * - PlayerAnimator:  sprite management
 * - AnimationState:  logic chọn animation dựa trên state
 * - PlayerCombatComponent: lấy attacking/lightning flags
 */
public class PlayerAnimationComponent implements PlayerComponent {

    private final PlayerAnimator animator;
    private final AnimationState animationState;

    /**
     * Constructor
     * @param animator PlayerAnimator instance
     * @param animationState AnimationState instance
     */
    public PlayerAnimationComponent(PlayerAnimator animator, AnimationState animationState) {
        this.animator = animator;
        this.animationState = animationState;
    }

    /**
     * Update animation mỗi frame
     *
     * Flow:
     * 1. Lấy combat state từ CombatComponent
     * 2. Set flags cho AnimationState
     * 3. AnimationState xác định action hiện tại
     * 4. Animator update frame
     */
    @Override
    public void update(Player player, float deltaTime) {
        // 1. Lấy combat component để check attacking state
        PlayerCombatComponent combat = player.getComponent(PlayerCombatComponent.class);

        // 2.  Sync combat flags với animation state
        animationState.setAttacking(combat.isAttacking());
        animationState.setUsingLightning(combat. isUsingLightning());

        // 3. Xác định action hiện tại (IDLE, RUN, JUMP, ATTACK, etc.)
        int currentAction = animationState.getCurrentAction();

        // 4. Update animator với action
        animator.update(currentAction);
    }

    @Override
    public void reset() {
        animator.resetAnimation();
    }

    // ===== Getters =====

    public PlayerAnimator getAnimator() {
        return animator;
    }

    public AnimationState getAnimationState() {
        return animationState;
    }
}