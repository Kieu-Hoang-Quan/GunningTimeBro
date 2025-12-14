package events;

import entity. Entity;

public class HealEvent extends GameEvent {

    public Entity target;
    public float healAmount;
    public float healthAfter;
    public float maxHealth;

    public HealEvent(Entity target, float healAmount, float healthAfter, float maxHealth) {
        super(target);
        this.target = target;
        this.healAmount = healAmount;
        this. healthAfter = healthAfter;
        this.maxHealth = maxHealth;
    }

    @Override
    public String getEventType() {
        return "HEAL";
    }

    public Entity getTarget() { return target; }
    public float getHealAmount() { return healAmount; }
    public float getHealthAfter() { return healthAfter; }
    public float getMaxHealth() { return maxHealth; }
}