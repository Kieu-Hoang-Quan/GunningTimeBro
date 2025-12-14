package entity.items.effects;

import entity.player.Player;

public class DamageBoostEffect implements ItemEffect {
    private float multiplier;
    private float duration;

    public DamageBoostEffect(float multiplier, float duration) {
        this.multiplier = multiplier;
        this.duration = duration;
    }

    @Override
    public void apply(Player player) {
        System.out.println("========================================");
        System.out.println("[DAMAGE BOOST PICKED UP]");
        System.out.println("Boost Amount: +" + (int)(multiplier * 100) + "%");
        System.out.println("Duration: " + duration + " seconds");

        player.getBuffSystem().addBuff("DAMAGE_BOOST", duration, multiplier);

        System.out.println("Current Multiplier: " + player.getMeleeComponent().getDamageMultiplier() + "x");
        System.out. println("========================================");
    }

    @Override
    public String getDescription() {
        return "+" + (int)(multiplier * 100) + "% damage for " + (int)duration + "s";
    }
}