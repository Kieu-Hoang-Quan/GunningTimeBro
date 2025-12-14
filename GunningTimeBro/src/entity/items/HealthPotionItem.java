package entity.items;

import entity.items.effects.HealEffect;
import entity.items.effects.ItemEffect;
import entity.player.Player;

public class HealthPotionItem implements Item {
    private String name;
    private ItemEffect effect;
    private String spritePath;

    public HealthPotionItem() {
        this.name = "Health Potion";
        this.effect = new HealEffect(50f);  // ✅ Fixed 50 HP
        this.spritePath = utilz.ItemSprites.Paths.HEALTH_POTION;
    }

    @Override
    public void apply(Player player) {
        effect.apply(player);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return effect.getDescription();
    }

    public String getSpritePath() {
        return spritePath;
    }
}