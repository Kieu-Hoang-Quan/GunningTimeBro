package entity.items;

import entity.items.effects.DamageBoostEffect;
import entity.items.effects.ItemEffect;
import entity.player.Player;

public class DamageBoostItem implements Item {
    private String name;
    private ItemEffect effect;
    private String spritePath;

    public DamageBoostItem() {
        this.name = "Damage Boost";
        this. effect = new DamageBoostEffect(0.5f, 60f);
        this.spritePath = utilz.ItemSprites. Paths.DAMAGE_BOOST;
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