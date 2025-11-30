package utilz;

import java.awt.geom.Rectangle2D;
import main.Game;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        return !IsSolid(x, y, lvlData) &&
                !IsSolid(x + width, y + height, lvlData) &&
                !IsSolid(x + width, y, lvlData) &&
                !IsSolid(x, y + height, lvlData);
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int rows = lvlData.length;
        int cols = lvlData[0].length;

        int mapWidthPixels  = cols * Game.TILES_SIZE;
        int mapHeightPixels = rows * Game.TILES_SIZE;

        // tường trái / phải
        if (x < 0 || x >= mapWidthPixels)
            return true;

        // trần
        if (y < 0)
            return true;

        // rơi xuống dưới map: để false để player rơi tự do
        if (y >= mapHeightPixels)
            return false;

        int xIndex = (int) (x / Game.TILES_SIZE);
        int yIndex = (int) (y / Game.TILES_SIZE);

        // ngoài grid thì coi như không có gạch
        if (yIndex < 0 || yIndex >= rows || xIndex < 0 || xIndex >= cols)
            return false;

        int value = lvlData[yIndex][xIndex];
        return value != 0; // 0 = air, khác 0 = solid
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

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);

        if (airSpeed > 0) {
            // Falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            // Jumping - hitting ceiling
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;

        return true;
    }
}