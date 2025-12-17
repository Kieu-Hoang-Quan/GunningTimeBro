package entity.player;

import entity. Entity;
import entity.ICollidable;
import entity. components.HealthComponent;
import entity.player.components.*;
import main.Game;
import utilz.PhysicsHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Player - Refactored với Component-Based Architecture.
 *
 * SOLID:
 * - SRP: Player chỉ còn điều phối components, không chứa logic nặng
 * - OCP: Thêm behavior mới = thêm component, không sửa Player
 * - DIP: Phụ thuộc interface PlayerComponent
 *
 * Responsibilities còn lại:
 * - Initialize và quản lý components
 * - Forward input/events tới components
 * - Render (delegate cho PlayerRender)
 */
public class Player extends Entity implements ICollidable {

    // ===== Component System =====
    private PlayerComponentRegistry components;

    // ===== Render (không phải component vì render riêng) =====
    private PlayerRender renderer;

    // ===== Input =====
    private InputHandler inputHandler;

    // ===== Direction =====
    private boolean flip = false;

    // ===== Level Data =====
    private int[][] lvlData;

    // ===== Systems (shared, không phải per-frame update) =====
    private HealthComponent healthComponent;
    private PlayerBuffSystem buffSystem;
    private PlayerAbilitySystem abilitySystem;
    private PlayerInventory inventory;

    /**
     * Constructor
     */
    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);

        // Init hitbox
        initHitbox(x, y, 6 * Game.SCALE, 15 * Game.SCALE);

        // Init systems
        initSystems();

        // Init components
        initComponents();

        // Init render
        renderer = new PlayerRender();
    }

    /**
     * Initialize shared systems
     */
    private void initSystems() {
        inputHandler = new InputHandler();
        healthComponent = new HealthComponent(this, 100f);
        buffSystem = new PlayerBuffSystem();
        abilitySystem = new PlayerAbilitySystem();
        inventory = new PlayerInventory();
    }

    /**
     * Initialize component registry và đăng ký components
     *
     * Thứ tự đăng ký quan trọng:
     * 1. Physics:  cần update position trước
     * 2. Combat: dùng position mới để check hit
     * 3. Animation: dựa trên combat state để chọn anim
     */
    private void initComponents() {
        components = new PlayerComponentRegistry();

        // 1. Physics Component
        PlayerPhysicsComponent physicsComp = new PlayerPhysicsComponent(this);
        components.register(PlayerPhysicsComponent.class, physicsComp);

        // 2. Combat Component
        PlayerCombatComponent combatComp = new PlayerCombatComponent(this, 15f);
        components.register(PlayerCombatComponent.class, combatComp);

        // 3. Animation Component (cần physics cho MovementState)
        PlayerAnimator animator = new PlayerAnimator();
        MovementState movementState = new MovementState(physicsComp. getPhysics(), inputHandler);
        AnimationState animationState = new AnimationState(movementState);
        PlayerAnimationComponent animComp = new PlayerAnimationComponent(animator, animationState);
        components.register(PlayerAnimationComponent.class, animComp);
    }

    /**
     * Update mỗi frame
     */
    public void update() {
        // Guard: không update nếu chưa có level data hoặc đã chết
        if (lvlData == null || ! healthComponent.isAlive()) {
            return;
        }

        float deltaTime = 1f / 60f;

        // Update tất cả components (theo thứ tự đăng ký)
        components.updateAll(this, deltaTime);

        // Update shared systems
        healthComponent.update(deltaTime);
        buffSystem.update(deltaTime);

        // Update flip direction dựa trên input
        updateFlip();
    }

    /**
     * Update facing direction
     */
    private void updateFlip() {
        if (inputHandler.isLeft()) flip = true;
        if (inputHandler.isRight()) flip = false;
    }

    /**
     * Render player
     */
    public void render(Graphics g, int camX) {
        renderer.render(g, this, camX);
    }

    /**
     * Load level data và set initial physics state
     */
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;

        // Check nếu spawn trên không thì set inAir
        if (! PhysicsHelper.isEntityOnFloor(hitbox, lvlData, Game.TILES_SIZE)) {
            getComponent(PlayerPhysicsComponent. class).getPhysics().setInAir(true);
        }
    }

    /**
     * Set enemies/obstacles cho physics và combat
     * Convert sang List<ICollidable> cho DIP
     */
    public void setEnemies(ArrayList<?  extends ICollidable> enemies) {
        List<ICollidable> collidables = new ArrayList<>(enemies);

        // Set cho physics (block movement)
        getComponent(PlayerPhysicsComponent.class).getPhysics().setObstacles(collidables);

        // Set cho combat (damage targets)
        getComponent(PlayerCombatComponent.class).setTargets(collidables);
    }

    /**
     * Reset input flags (khi window lose focus)
     */
    public void resetDirBooleans() {
        inputHandler. resetAll();
    }

    // ===== Combat Entry Points =====

    /**
     * Bắt đầu attack thường
     */
    public void setAttacking(boolean attacking) {
        if (attacking) {
            getComponent(PlayerCombatComponent.class).startAttack(this);
        }
    }

    /**
     * Bắt đầu lightning attack
     */
    public void useLightningPower() {
        getComponent(PlayerCombatComponent.class).startLightningAttack(this);
    }

    // ===== Component Access =====

    /**
     * Generic component getter
     */
    public <T extends PlayerComponent> T getComponent(Class<T> type) {
        return components.get(type);
    }

    // ===== ICollidable Implementation =====

    @Override
    public boolean isActive() {
        return healthComponent. isAlive();
    }

    // ===== Getters =====

    public HealthComponent getHealthComponent() { return healthComponent; }
    public PlayerBuffSystem getBuffSystem() { return buffSystem; }
    public PlayerAbilitySystem getAbilitySystem() { return abilitySystem; }
    public PlayerInventory getInventory() { return inventory; }
    public InputHandler getInputHandler() { return inputHandler; }
    public int[][] getLevelData() { return lvlData; }
    public boolean isFlip() { return flip; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /**
     * Get animator từ animation component
     */
    public PlayerAnimator getAnimator() {
        return getComponent(PlayerAnimationComponent.class).getAnimator();
    }

    /**
     * Get animation state từ animation component
     */
    public AnimationState getAnimationState() {
        return getComponent(PlayerAnimationComponent.class).getAnimationState();
    }

    /**
     * Get melee component từ combat component
     */
    public entity.components.MeleeComponent getMeleeComponent() {
        return getComponent(PlayerCombatComponent.class).getMeleeComponent();
    }

    // ===== Setters =====

    public void setFlip(boolean flip) { this.flip = flip; }
    public void setLeft(boolean left) { inputHandler.setLeft(left); }
    public void setRight(boolean right) { inputHandler.setRight(right); }
    public void setJump(boolean jump) { inputHandler.setJump(jump); }
    public void setDown(boolean down) { inputHandler.setDown(down); }
}