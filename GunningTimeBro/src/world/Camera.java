package world;

import java.awt.geom.Rectangle2D;
import main.Game;
import map.TileMap;

public class Camera {

    private int x;

    // ✅ ADD:  Spectator mode
    public boolean spectatorMode = false;

    public int getX() {
        return x;
    }

    public void reset() {
        x = 0;
    }

    public void update(Rectangle2D. Float playerHitbox, TileMap tileMap) {
        // ✅ CHANGED: Chỉ follow player khi KHÔNG ở spectator mode
        if (spectatorMode) {
            // Spectator mode: camera không follow player
            return;
        }

        // Normal mode: follow player
        float playerCenterX = playerHitbox. x + playerHitbox.width / 2.0f;
        int halfScreen = Game.GAME_WIDTH / 2;
        int mapWidthPixels = tileMap.getMapWidthPixels();
        int maxCamX = Math.max(0, mapWidthPixels - Game.GAME_WIDTH);
        int desired = (int) (playerCenterX - halfScreen);
        if (desired < 0) desired = 0;
        if (desired > maxCamX) desired = maxCamX;
        x = desired;
    }

    // ✅ ADD: Di chuyển camera thủ công
    public void moveCamera(int delta, TileMap tileMap) {
        x += delta;
        int maxCamX = Math.max(0, tileMap.getMapWidthPixels() - Game.GAME_WIDTH);
        if (x < 0) x = 0;
        if (x > maxCamX) x = maxCamX;
    }
}