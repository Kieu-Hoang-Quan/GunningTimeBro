package utilz;

import java.awt.geom.Rectangle2D;
import main.Game;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        return ! IsSolid(x, y, lvlData) &&
                !IsSolid(x + width, y + height, lvlData) &&
                !IsSolid(x + width, y, lvlData) &&
                !IsSolid(x, y + height, lvlData);
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int rows = lvlData.length;
        int cols = lvlData[0].length;

        // Calculate the actual map boundaries
        int mapWidthPixels = cols * Game.TILES_SIZE;
        int mapHeightPixels = rows * Game. TILES_SIZE;

        // Map starts at baseY
        final int GAME_HEIGHT = 324;
        int baseY = GAME_HEIGHT - mapHeightPixels;

        // Check if out of horizontal bounds
        if (x < 0 || x >= mapWidthPixels)
            return true;

        // Check if out of vertical bounds (adjusted for baseY)
        if (y < baseY || y >= GAME_HEIGHT)
            return true;

        // Convert world coordinates to grid coordinates
        int xIndex = (int) (x / Game.TILES_SIZE);
        int yIndex = (int) ((y - baseY) / Game. TILES_SIZE);  // Adjust for baseY!

        // Bounds check for array access
        if (yIndex < 0 || yIndex >= rows || xIndex < 0 || xIndex >= cols)
            return true;

        int value = lvlData[yIndex][xIndex];

        // Tile 0 = empty/air (not solid)
        if (value == 0)
            return false;

        return true;  // Any non-zero tile is solid
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game. TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else
            // Left
            return currentTile * Game.TILES_SIZE;
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D. Float hitbox, float airSpeed) {
        final int GAME_HEIGHT = 324;
        int rows = 10;  // Your level has 10 rows
        int mapHeightPixels = rows * Game. TILES_SIZE;
        int baseY = GAME_HEIGHT - mapHeightPixels;

        // Adjust hitbox. y for baseY before calculating tile
        int currentTile = (int) ((hitbox.y - baseY) / Game.TILES_SIZE);

        if (airSpeed > 0) {
            // Falling - touching floor
            int tileYPos = baseY + currentTile * Game. TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            // Jumping - hitting ceiling
            return baseY + currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Check the pixel below bottom-left and bottom-right
        if (! IsSolid(hitbox. x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox. height + 1, lvlData))
                return false;

        return true;
    }
}