package entity. components;

import entity.Entity;
import entity.player.Player;
import entity.enemy.*;


public class MeleeComponent {

    private Entity owner;
    private float baseDamage;
    private float damageMultiplier;
    private float attackCooldown;
    private float timeSinceAttack;
    private int comboCount;
    private int maxCombo = 5;

    public MeleeComponent(Entity owner, float baseDamage) {
        this.owner = owner;
        this.baseDamage = baseDamage;
        this.damageMultiplier = 1.0f;
        this.attackCooldown = 0.5f;
        this.timeSinceAttack = attackCooldown;
        this. comboCount = 0;
    }

    public void update(float deltaTime) {
        timeSinceAttack += deltaTime;

        // Reset combo after 2 seconds of no attacks
        if (timeSinceAttack > 2.0f) {
            comboCount = 0;
        }
    }


    public void attack(Entity target) {
        if (timeSinceAttack < attackCooldown) return;
        if (target == null) return;

        // Calculate damage:  base * multiplier * combo
        float comboBonus = 1 + (comboCount * 0.1f);
        float finalDamage = baseDamage * damageMultiplier * comboBonus;

        boolean success = applyDamage(target, finalDamage);

        if (success) {
            timeSinceAttack = 0;
            comboCount++;
            if (comboCount > maxCombo) {
                comboCount = maxCombo;
            }

            System.out.println("[MELEE] Attack!  Damage: " + (int)finalDamage +
                    " (Base:" + (int)baseDamage +
                    " x" + damageMultiplier + " boost" +
                    " x" + comboBonus + " combo)");
        }
    }

    private boolean applyDamage(Entity target, float damage) {
        if (target instanceof Player) {
            Player player = (Player) target;
            if (player.getHealthComponent().isAlive()) {
                player.getHealthComponent().takeDamage(damage, owner);
                return true;
            }
        } else if (target instanceof Enemy) {
            Enemy enemy = (Enemy) target;
            if (enemy.getHealthComponent().isAlive()) {
                enemy.getHealthComponent().takeDamage(damage, owner);
                return true;
            }
        }
        return false;
    }

    public void setDamageMultiplier(float multiplier) {
        this.damageMultiplier = multiplier;
        System.out.println("[MELEE] Damage multiplier:   " + multiplier + "x");
    }

    public void addDamageBoost(float amount) {
        this.damageMultiplier += amount;
        System.out.println("[MELEE] Damage boost!  Now: " + damageMultiplier + "x");
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
}