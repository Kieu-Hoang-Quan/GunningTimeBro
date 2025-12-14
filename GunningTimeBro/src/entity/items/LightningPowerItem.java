package entity.items;

import entity.player.Player;

/**
 * Lightning Power - Collectible item
 * Stored in inventory, activated with K key
 */
public class LightningPowerItem implements Item {
    private String name;
    private String spritePath;

    public LightningPowerItem() {
        this.name = "Lightning Power";
        this. spritePath = utilz.ItemSprites.Paths.LIGHTNING_POWER;
    }

    @Override
    public void apply(Player player) {
        // ✅ Add to inventory instead of instant buff
        boolean added = player.getAbilitySystem().addLightningCharge();

        if (added) {
            System.out.println("[Lightning] Collected!  Total charges: " +
                    player.getAbilitySystem().getLightningCharges());
        } else {
            System.out. println("[Lightning] Already at max capacity (3)");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Press K to activate:  Max damage + 2x range for 10s";
    }

    public String getSpritePath() {
        return spritePath;
    }
}