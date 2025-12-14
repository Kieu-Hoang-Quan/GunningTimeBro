package world;

import java.awt.geom.Rectangle2D;

import main.Game;
import map.TileMap;

public class Camera {

    private int x;   // cameraX, đang chỉ scroll ngang

    public int getX() {
        return x;
    }

    public void reset() {
        x = 0;
    }

    public void update(Rectangle2D.Float playerHitbox, TileMap tileMap) {
        // tâm X của player trong world
        float playerCenterX = playerHitbox.x + playerHitbox.width / 2.0f;

        int halfScreen = Game.GAME_WIDTH / 2;

        int mapWidthPixels = tileMap.getMapWidthPixels();
        int maxCamX = Math.max(0, mapWidthPixels - Game.GAME_WIDTH);

        int desired = (int) (playerCenterX - halfScreen);

        if (desired < 0) desired = 0;
        if (desired > maxCamX) desired = maxCamX;

        x = desired;
    }
}
