package entity.items.effects;

import entity.player.Player;

/**
 * Item effect interface - NO getName (chỉ Item có)
 */
public interface ItemEffect {
    void apply(Player player);
    String getDescription();
}