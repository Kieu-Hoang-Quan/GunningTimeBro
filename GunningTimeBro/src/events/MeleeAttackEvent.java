package events;

import entity.Entity;

public class MeleeAttackEvent extends GameEvent {

    public Entity attacker;
    public Entity target;
    public float damage;
    public int comboCount;

    public MeleeAttackEvent(Entity attacker, Entity target, float damage, int comboCount) {
        super(attacker);
        this.attacker = attacker;
        this.target = target;
        this.damage = damage;
        this.comboCount = comboCount;
    }

    @Override
    public String getEventType() {
        return "MELEE_ATTACK";
    }

    public Entity getAttacker() { return attacker; }
    public Entity getTarget() { return target; }
    public float getDamage() { return damage; }
    public int getComboCount() { return comboCount; }
}