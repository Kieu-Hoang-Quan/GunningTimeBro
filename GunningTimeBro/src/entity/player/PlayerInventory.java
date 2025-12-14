package entity.player;

import entity.items.Item;
import java.util.HashMap;
import java.util.Map;

public class PlayerInventory {
    private Map<String, Integer> itemCounts;
    private Map<String, Item> itemTemplates;
    private static final int MAX_STACK_SIZE = 99;

    public PlayerInventory() {
        this.itemCounts = new HashMap<>();
        this.itemTemplates = new HashMap<>();
    }

    public boolean addItem(Item item) {
        String itemType = item.getClass().getSimpleName();
        int currentCount = itemCounts.getOrDefault(itemType, 0);

        if (currentCount >= MAX_STACK_SIZE) return false;

        itemCounts.put(itemType, currentCount + 1);
        itemTemplates.put(itemType, item);
        return true;
    }

    public boolean useItem(String itemType, Player player) {
        int count = itemCounts.getOrDefault(itemType, 0);
        if (count <= 0) return false;

        Item item = itemTemplates.get(itemType);
        if (item != null) {
            item.apply(player);
            itemCounts.put(itemType, count - 1);
            return true;
        }
        return false;
    }

    public int getItemCount(String itemType) {
        return itemCounts. getOrDefault(itemType, 0);
    }

    public void clear() {
        itemCounts. clear();
        itemTemplates. clear();
    }
}