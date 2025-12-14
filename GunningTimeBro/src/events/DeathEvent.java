package events;

import entity. Entity;

public class DeathEvent extends GameEvent {

    public Entity victim;
    public Entity killer;

    public DeathEvent(Entity victim, Entity killer) {
        super(victim);
        this.victim = victim;
        this.killer = killer;
    }

    @Override
    public String getEventType() {
        return "DEATH";
    }

    public Entity getVictim() { return victim; }
    public Entity getKiller() { return killer; }
}