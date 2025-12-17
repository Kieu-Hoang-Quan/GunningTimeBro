package gamestates.systems;

import entity.player.Player;

public class PlayerSystem {
    private final Player player;

    public PlayerSystem(Player player) { this.player = player; }
    public void update() { player.update(); }
    public Player getPlayer() { return player; }
}