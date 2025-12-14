package events;

import entity.Entity;

public class DamageEvent extends GameEvent {

    public Entity victim;
    public Entity attacker;
    public float damage;
    public float healthRemaining;
    public float maxHealth;

    public DamageEvent(Entity victim, Entity attacker, float damage,
                       float healthRemaining, float maxHealth) {
        super(victim);
        this.victim = victim;
        this.attacker = attacker;
        this.damage = damage;
        this.healthRemaining = healthRemaining;
        this.maxHealth = maxHealth;
    }

    @Override
    public String getEventType() {
        return "DAMAGE";
    }

    public Entity getVictim() { return victim; }
    public Entity getAttacker() { return attacker; }
    public float getDamage() { return damage; }
    public float getHealthRemaining() { return healthRemaining; }
    public float getMaxHealth() { return maxHealth; }
    public float getHealthPercent() { return healthRemaining / maxHealth; }
}