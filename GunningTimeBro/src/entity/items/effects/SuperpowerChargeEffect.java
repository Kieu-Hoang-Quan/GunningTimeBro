package entity.items. effects;

import entity.player. Player;

public class SuperpowerChargeEffect implements ItemEffect {
    private int charges;

    public SuperpowerChargeEffect(int charges) {
        this.charges = charges;
    }

    @Override
    public void apply(Player player) {
        player.getAbilitySystem().addLightningCharge();
    }

    @Override
    public String getDescription() {
        return "+" + charges + " Lightning charge";
    }
}