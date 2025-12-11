package entity.enemy;

import entity.Entity; // Import class cha Entity
import entity.player.Player;
import main.Game;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static utilz.Constants.EnemyConstants.*;

public abstract class Enemies extends Entity {

    // Components (Giống Player)
    protected EnemyPhysics physics;
    protected EnemyAnimator animator;
    protected EnemyRender renderer;

    // Stats & State
    protected int enemyType;
    protected int enemyState = IDLE;
    protected boolean active = true;
    protected boolean flip = false;

    // Offset vẽ
    protected int xDrawOffset;
    protected int yDrawOffset;

    // Combat
    protected Rectangle2D.Float attackBox;
    public boolean attackChecked = false;
    public int attackDamage = 20;

    // AI Params
    protected float chaseRange = 120f;
    protected float attackTriggerRange = 45f;

    // Constructor
    public Enemies(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;

        // Init Components
        this.physics = new EnemyPhysics(this);
        this.animator = new EnemyAnimator(this);
        this.renderer = new EnemyRender();

        initHitbox(x, y, width - 10, height - 5);
        attackBox = new Rectangle2D.Float(x, y, width, height);
    }

    public void update(int[][] lvlData, Player player) {
        // 1. Update Physics (Di chuyển)
        physics.update(lvlData);

        // 2. Update AI Logic (Quyết định hành động)
        updateStateAI(player);

        // 3. Update Combat Box (Cập nhật theo vị trí mới)
        updateAttackBox();

        // 4. Update Animation
        animator.update();
    }

    // Render gọi Component
    public void render(Graphics g, BufferedImage[][] sprites, int xOff) {
        renderer.render(g, this, sprites, xOff);
    }

    // Logic AI (Bạn muốn giữ nguyên không đổi để an toàn)
    protected void updateStateAI(Player player) {
        if (enemyState == DEAD) return;
        if (enemyState == HIT) {
            // Chờ hết animation Hit
            if (animator.getAniIndex() >= GetSpriteAmount(enemyType, HIT) - 1)
                setNewState(RUNNING);
            return;
        }

        float px = player.getHitbox().x;
        float ex = hitbox.x;
        float distX = Math.abs(px - ex);
        float distY = Math.abs(player.getHitbox().y - hitbox.y);
        boolean sameLevel = distY < hitbox.height * 0.6f;

        // Tấn công
        if (sameLevel && distX < attackTriggerRange) {
            flip = px < ex;
            setNewState(ATTACK);
            return;
        }

        // Đuổi theo
        if (sameLevel && distX < chaseRange) {
            flip = px < ex;
            setNewState(RUNNING);
            return;
        }

        // Đi tuần tra (Chạy qua lại)
        setNewState(RUNNING);
    }

    protected void updateAttackBox() {
        float w = hitbox.width;
        float h = hitbox.height;

        // Logic Attack Box động
        float effectiveRange = w * 0.6f;
        if (enemyState == ATTACK) {
            // Có thể thêm logic range động ở đây nếu muốn
            effectiveRange = w;
        }

        attackBox.width = effectiveRange;
        attackBox.height = h * 0.5f;
        attackBox.y = hitbox.y + (h - attackBox.height) / 2;

        if (flip) attackBox.x = hitbox.x - attackBox.width + 10;
        else attackBox.x = hitbox.x + w - 10;
    }

    // --- Getters / Setters / Helpers ---

    public void setNewState(int state) {
        if (this.enemyState == state) return;
        this.enemyState = state;
        animator.reset(); // Reset thông qua component
        attackChecked = false;
    }

    public boolean isAttackFrame() {
        // Frame thứ 3 (tức là hình số 4) là frame gây damage
        return animator.getAniIndex() == 3;
    }

    public void drawDebug(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        g.setColor(Color.BLUE);
        g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public void setActive(boolean active) { this.active = active; }
    public boolean isActive() { return active; }
    public boolean isFlip() { return flip; }
    public void setFlip(boolean flip) { this.flip = flip; }
    public int getEnemyState() { return enemyState; }
    public int getEnemyType() { return enemyType; }
    public EnemyAnimator getAnimator() { return animator; }
    public Rectangle2D.Float getAttackBox() { return attackBox; }
    public int getXDrawOffset() { return xDrawOffset; }
    public int getYDrawOffset() { return yDrawOffset; }
    public void setAttackChecked(boolean checked) { this.attackChecked = checked; }
}