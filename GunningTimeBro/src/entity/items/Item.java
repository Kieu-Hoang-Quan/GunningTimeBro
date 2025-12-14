package entity.items;

import entity.player.Player;

public interface Item {
    void apply(Player player);
    String getName();
    String getDescription();
}