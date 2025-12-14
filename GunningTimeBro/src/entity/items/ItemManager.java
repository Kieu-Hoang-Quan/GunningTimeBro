package entity. items;

import entity.player.Player;
import entity.items.LevelItemConfig. ItemSpawnPoint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class ItemManager {
    private ArrayList<ItemEntity> items;

    public ItemManager() {
        this.items = new ArrayList<>();
    }

    public void loadItemsForLevel(int levelNumber) {
        items.clear();

        ItemSpawnPoint[] spawnPoints = LevelItemConfig.getItemsForLevel(levelNumber);

        for (ItemSpawnPoint spawn : spawnPoints) {
            spawnItem(spawn.x, spawn.y, spawn.item, spawn.sprite);
        }

        System.out.println("[ItemManager] Loaded " + items.size() + " items");
    }

    public void spawnItem(float x, float y, Item item, BufferedImage sprite) {
        items.add(new ItemEntity(x, y, item, sprite));
    }

    public void update(Player player) {
        Iterator<ItemEntity> iterator = items. iterator();
        while (iterator. hasNext()) {
            ItemEntity itemEntity = iterator.next();
            itemEntity.update(player);

            if (itemEntity.isCollected()) {
                iterator. remove();
            }
        }
    }

    public void render(Graphics g, int camX) {
        for (ItemEntity itemEntity : items) {
            itemEntity.render(g, camX);
        }
    }

    public void clear() {
        items.clear();
    }
}