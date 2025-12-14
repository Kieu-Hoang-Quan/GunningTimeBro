package entity.components;

import entity.Entity;
import events.*;

public class HealthComponent {

    private Entity owner;
    private float health;
    private float maxHealth;
    private boolean alive;
    private boolean invincible;
    private float invincibleTimer;
    private float invincibleTime = 0.5f;

    private EventBus eventBus;

    public HealthComponent(Entity owner, float maxHealth) {
        this.owner = owner;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this. alive = true;
        this. invincible = false;
        this.eventBus = EventBus.getInstance();
    }

    public void update(float deltaTime) {
        if (invincible) {
            invincibleTimer += deltaTime;
            if (invincibleTimer >= invincibleTime) {
                invincible = false;
                invincibleTimer = 0;
            }
        }
    }

    public void takeDamage(float damage, Entity attacker) {
        if (!alive || invincible) return;

        health -= damage;
        if (health < 0) health = 0;

        invincible = true;
        invincibleTimer = 0;

        eventBus. publish(new DamageEvent(owner, attacker, damage, health, maxHealth));

        if (health <= 0) {
            die(attacker);
        }
    }

    public void heal(float amount) {
        if (!alive) return;

        health += amount;
        if (health > maxHealth) health = maxHealth;

        eventBus.publish(new HealEvent(owner, amount, health, maxHealth));
    }

    public void increaseMaxHealth(float amount) {
        maxHealth += amount;
        health += amount;
    }

    private void die(Entity killer) {
        alive = false;
        eventBus.publish(new DeathEvent(owner, killer));
    }

    public void revive() {
        alive = true;
        health = maxHealth;
        invincible = false;
    }

    public float getHealth() { return health; }
    public float getPercent() { return health / maxHealth; }
    public boolean isAlive() { return alive; }
    public boolean isInvincible() { return invincible; }
    public float getMaxHealth() { return maxHealth; }
}