package events;

import entity.player.Player;
import entity.items.Item;

public class ItemPickupEvent extends GameEvent {

    private Player player;
    public Item item;

    public ItemPickupEvent(Player player, Item item) {
        super(player);
        this.player = player;
        this.item = item;
    }

    @Override
    public String getEventType() {
        return "ITEM_PICKUP";
    }

    public Player getPlayer() { return player; }
    public Item getItem() { return item; }
}