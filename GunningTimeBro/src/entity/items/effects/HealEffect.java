package entity.items.effects;

import entity.player.Player;

public class HealEffect implements ItemEffect {
    private float healAmount;

    public HealEffect(float healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void apply(Player player) {
        player.getHealthComponent().heal(healAmount);
    }

    @Override
    public String getDescription() {
        return "Restore " + (int)healAmount + " HP";
    }
}