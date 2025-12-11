package entity;

import main.Game;
import utilz.Constants.EnemyConstants;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.HelpMethods.*;

public abstract class Enemies extends Entity {

    protected int aniIndex, aniTick;
    protected int walkAniSpeed = 15;
    protected int attackAniSpeed = 28;
    protected int holdAttackFrameTicks = 7;
    protected int holdTick = 0;

    protected int enemyState = IDLE;
    protected int enemyType;

    protected boolean inAir = true;
    protected float airSpeed = 0f;
    protected float gravity = 0.04f;
    protected float walkSpeed = 1.0f;

    protected boolean firstUpdate = true;
    protected boolean flip = false;

    protected int[][] lvlData;

    // Combat
    protected Rectangle2D.Float attackBox;
    public boolean attackChecked = false;
    protected int attackDamage = 20;
    protected float attackRangeMultiplier = 1.1f;
    protected int hp = 100;

    // AI
    protected float chaseRange = 120f;
    protected float attackTriggerRange = 45f;

    public Enemies(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;

        initHitbox(x, y, width - 10, height - 5);
        attackBox = new Rectangle2D.Float(hitbox.x, hitbox.y, hitbox.width, hitbox.height * 0.3f);
    }

    public void update(int[][] lvlData) {

        this.lvlData = lvlData;

        if (enemyState == DEAD) {
            updateAnimationTick(); // chỉ chạy animation death rồi đứng im
            return;
        }

        if (firstUpdate) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                inAir = false;
                enemyState = RUNNING;
            }
            firstUpdate = false;
        }

        updateStateAI();
        updatePos();
        updateAttackBox();
        updateAnimationTick();
    }

    protected void updateStateAI() {

        if (enemyState == DEAD) return;

        if (enemyState == HIT) {
            if (aniIndex == EnemyConstants.GetSpriteAmount(enemyType, HIT) - 1)
                setNewState(RUNNING);
            return;
        }

        Player player = Game.getPlayerStatic();
        float px = player.getHitbox().x;
        float py = player.getHitbox().y;
        float ex = hitbox.x;
        float ey = hitbox.y;

        float distX = Math.abs(px - ex);
        float distY = Math.abs(py - ey);

        boolean sameLevel = distY < hitbox.height * 0.6f;

        if (sameLevel && distX < attackTriggerRange) {
            flip = px < ex;
            setNewState(ATTACK);
            return;
        }

        if (sameLevel && distX < chaseRange) {
            flip = px < ex;
            setNewState(RUNNING);
            return;
        }

        if (enemyState != RUNNING)
            setNewState(RUNNING);
    }

    protected void updatePos() {

        if (enemyState == ATTACK || enemyState == DEAD) return;

        float xSpeed = flip ? -walkSpeed : walkSpeed;

        if (!inAir && !IsEntityOnFloor(hitbox, lvlData))
            inAir = true;

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                airSpeed = 0;
                inAir = false;
                setNewState(RUNNING);
            }
        }

        float newX = hitbox.x + xSpeed;

        if (CanMoveHere(newX, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (IsFloor(hitbox, xSpeed, lvlData))
                hitbox.x = newX;
            else
                flip = !flip;
        } else {
            hitbox.x += flip ? 1 : -1;
            flip = !flip;
        }
    }

    private void updateAttackBox() {

        float baseRange = hitbox.width * attackRangeMultiplier;
        float effectiveRange = hitbox.width * 0.6f;

        if (enemyState == ATTACK) {
            int total = EnemyConstants.GetSpriteAmount(enemyType, ATTACK);
            int idx = Math.max(0, Math.min(aniIndex, total - 1));

            int earlyEnd = total / 4;
            int midEnd = total * 3 / 4;

            if (idx <= earlyEnd)        effectiveRange = hitbox.width * 0.6f;
            else if (idx <= midEnd)     effectiveRange = hitbox.width * 0.95f;
            else                        effectiveRange = baseRange;

        } else {
            effectiveRange = hitbox.width * 0.4f;
        }

        float boxH = hitbox.height * 0.30f;

        attackBox.width = effectiveRange;
        attackBox.height = boxH;
        attackBox.y = hitbox.y + hitbox.height * 0.45f;

        attackBox.x = flip ? hitbox.x - effectiveRange : hitbox.x + hitbox.width;
    }

    protected void updateAnimationTick() {

        int speed = (enemyState == ATTACK) ? attackAniSpeed : walkAniSpeed;

        aniTick++;

        if (aniTick >= speed) {
            aniTick = 0;
            aniIndex++;

            int maxFrames = EnemyConstants.GetSpriteAmount(enemyType, enemyState);

            if (enemyState == ATTACK) {

                if (aniIndex >= maxFrames) {
                    aniIndex = maxFrames - 1;
                    holdTick++;

                    if (holdTick >= holdAttackFrameTicks) {
                        holdTick = 0;
                        attackChecked = false;
                        setNewState(RUNNING);
                    }
                    return;
                }

            }
            else if (enemyState == DEAD) {

                if (aniIndex >= maxFrames) {
                    aniIndex = maxFrames - 1; // đứng im frame cuối
                }
                return;
            }
            else {
                if (aniIndex >= maxFrames) aniIndex = 0;
            }
        }
    }

    public boolean isAttackFrame() {
        int total = EnemyConstants.GetSpriteAmount(enemyType, ATTACK);
        if (total <= 2) return aniIndex == total - 1;
        return aniIndex == total - 2;
    }

    public void receiveDamage(int dmg) {
        if (enemyState == DEAD) return;

        hp -= dmg;
        if (hp <= 0) {
            hp = 0;
            setNewState(DEAD); // làm chết
        } else {
            setNewState(HIT);
        }
    }

    protected void setNewState(int state) {
        if (this.enemyState == state) return;

        this.enemyState = state;
        aniTick = 0;
        aniIndex = 0;
        attackChecked = false;
    }

    // luôn hiển thị cả khi chết
    public boolean isAlive() {
        return true;
    }

    public int getAniIndex() { return aniIndex; }
    public int getEnemyState() { return enemyState; }
    public boolean isFlip() { return flip; }
    public Rectangle2D.Float getHitbox() { return hitbox; }
    public Rectangle2D.Float getAttackBox() { return attackBox; }

    public void drawDebug(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);

        g.setColor(Color.BLUE);
        g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }
}







