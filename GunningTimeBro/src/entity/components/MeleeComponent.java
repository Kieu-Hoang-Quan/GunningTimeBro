package entity.components;

import entity.Entity;
import entity.player.Player;
import entity.enemy.Enemy;
import events.EventBus;
import events.MeleeAttackEvent;

public class MeleeComponent {

    private Entity owner;
    private float baseDamage;
    private float damageMultiplier;
    private float attackCooldown;
    private float timeSinceAttack;
    private int comboCount;
    private int maxCombo = 5;
    private EventBus eventBus;

    private static final float NORMAL_ATTACK_MULTIPLIER = 1.0f;
    private static final float RUNNING_ATTACK_MULTIPLIER = 1.67f;
    private static final float LIGHTNING_ATTACK_MULTIPLIER = 6.67f;

    public MeleeComponent(Entity owner, float baseDamage) {
        this.owner = owner;
        this.baseDamage = baseDamage;
        this.damageMultiplier = 1.0f;
        this.attackCooldown = 0.5f;
        this.timeSinceAttack = attackCooldown;
        this. comboCount = 0;
        this.eventBus = EventBus.getInstance();
    }

    public void update(float deltaTime) {
        timeSinceAttack += deltaTime;

        if (timeSinceAttack > 2.0f) {
            comboCount = 0;
        }
    }

    public float calculateDamage(int attackType) {
        float typeMultiplier = getAttackTypeMultiplier(attackType);
        float comboBonus = 1 + (comboCount * 0.1f);
        float finalDamage = baseDamage * typeMultiplier * damageMultiplier * comboBonus;
        return finalDamage;
    }

    private float getAttackTypeMultiplier(int attackType) {
        switch (attackType) {
            case 6:
                return LIGHTNING_ATTACK_MULTIPLIER;
            case 5:
                return RUNNING_ATTACK_MULTIPLIER;
            case 4:
                return NORMAL_ATTACK_MULTIPLIER;
            default:
                return NORMAL_ATTACK_MULTIPLIER;
        }
    }

    public void attack(Entity target) {
        if (timeSinceAttack < attackCooldown) return;
        if (target == null) return;

        float comboBonus = 1 + (comboCount * 0.1f);
        float finalDamage = baseDamage * damageMultiplier * comboBonus;

        boolean success = applyDamage(target, finalDamage);

        if (success) {
            timeSinceAttack = 0;
            comboCount++;
            if (comboCount > maxCombo) {
                comboCount = maxCombo;
            }

            eventBus.publish(new MeleeAttackEvent(owner, target, finalDamage, comboCount));
        }
    }

    private boolean applyDamage(Entity target, float damage) {
        if (target instanceof Player) {
            Player player = (Player) target;
            if (player.getHealthComponent().isAlive()) {
                player. getHealthComponent().takeDamage(damage, owner);
                return true;
            }
        } else if (target instanceof Enemy) {
            Enemy enemy = (Enemy) target;
            if (enemy. getHealthComponent().isAlive()) {
                enemy.getHealthComponent().takeDamage(damage, owner);
                return true;
            }
        }
        return false;
    }

    public void setDamageMultiplier(float multiplier) {
        this.damageMultiplier = multiplier;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public void resetCombo() {
        comboCount = 0;
    }

    public int getComboCount() {
        return comboCount;
    }

    public float getCurrentDamage() {
        return baseDamage * damageMultiplier;
    }

    public void incrementCombo() {
        comboCount++;
        if (comboCount > maxCombo) {
            comboCount = maxCombo;
        }
    }
}