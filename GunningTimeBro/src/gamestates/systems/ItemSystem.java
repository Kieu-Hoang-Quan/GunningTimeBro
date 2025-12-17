package gamestates.systems;

import entity.items.ItemManager;
import entity.player.Player;

public class ItemSystem {
    private final ItemManager itemManager;
    private final Player player;

    public ItemSystem(ItemManager itemManager, Player player) {
        this.itemManager = itemManager;
        this.player = player;
    }

    public void update() { itemManager.update(player); }
}