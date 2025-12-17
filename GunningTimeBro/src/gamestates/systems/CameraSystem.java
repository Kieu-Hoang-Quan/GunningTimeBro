package gamestates. systems;

import world.Camera;
import map.TileMap;
import main.Game;

import java.awt.*;

public class CameraSystem {
    private final Camera camera = new Camera();
    private final TileMap tileMap;

    public CameraSystem(TileMap tileMap) { this.tileMap = tileMap; }

    public void update(entity.player.Player player) {
        camera.update(player. getHitbox(), tileMap);
    }

    public void moveLeft() { camera.moveCamera(-30, tileMap); }
    public void moveRight() { camera.moveCamera(30, tileMap); }
    public void toggleSpectator() { camera.spectatorMode = !camera.spectatorMode; }
    public boolean isSpectatorMode() { return camera.spectatorMode; }
    public int getCamX() { return camera.getX(); }

    public void drawSpectatorUI(Graphics2D g, TileMap map) {
        if (!camera.spectatorMode) return;
        g.setColor(new Color(255, 255, 0, 200));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("SPECTATOR MODE - Arrow Keys:  Move | V: Exit", 20, 50);

        int mapWidth = map.getMapWidthPixels();
        int maxCamX = Math.max(0, mapWidth - Game.GAME_WIDTH);
        float progress = maxCamX > 0 ? (float)camera.getX() / maxCamX : 0;

        g.setColor(Color. WHITE);
        g.drawString(String.format("Position: %d / %d (%.0f%%)", camera.getX(), maxCamX, progress * 100), 20, 80);
    }
}