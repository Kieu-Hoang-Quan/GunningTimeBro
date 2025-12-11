package utilz;

import java.awt.geom.Rectangle2D;

public class PhysicsHelper {


    public static boolean canMoveHere(float x, float y, float width, float height,
                                      int[][] lvlData, int tileSize) {
        return ! isSolid(x, y, lvlData, tileSize) &&
                !isSolid(x + width, y + height, lvlData, tileSize) &&
                !isSolid(x + width, y, lvlData, tileSize) &&
                ! isSolid(x, y + height, lvlData, tileSize);
    }


    public static boolean isSolid(float x, float y, int[][] lvlData, int tileSize) {
        int rows = lvlData.length;
        int cols = lvlData[0].length;

        int mapWidthPixels = cols * tileSize;
        int mapHeightPixels = rows * tileSize;

        // Out of bounds checks
        if (x < 0 || x >= mapWidthPixels) return true;
        if (y < 0) return true;
        if (y >= mapHeightPixels) return false;  // Fall through bottom

        // Get tile indices
        int xIndex = (int) (x / tileSize);
        int yIndex = (int) (y / tileSize);

        // Bounds check
        if (yIndex < 0 || yIndex >= rows || xIndex < 0 || xIndex >= cols) {
            return false;
        }

        int value = lvlData[yIndex][xIndex];
        return value != 0;  // 0 = air, non-zero = solid
    }

    public static float getEntityXPosNextToWall(Rectangle2D. Float hitbox,
                                                float xSpeed, int tileSize) {
        int currentTile = (int) (hitbox.x / tileSize);

        if (xSpeed > 0) {
            // Moving right - snap to left edge of wall
            int tileXPos = currentTile * tileSize;
            int xOffset = (int) (tileSize - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            // Moving left - snap to right edge of wall
            return currentTile * tileSize;
        }
    }

    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox,
                                                           float airSpeed, int tileSize) {
        int currentTile = (int) (hitbox.y / tileSize);

        if (airSpeed > 0) {
            // Falling
            int tileYPos = currentTile * tileSize;
            int yOffset = (int) (tileSize - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            // Jumping
            return currentTile * tileSize;
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox,
                                          int[][] lvlData, int tileSize) {
        boolean leftCorner = isSolid(hitbox.x, hitbox.y + hitbox.height + 1,
                lvlData, tileSize);
        boolean rightCorner = isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox. height + 1,
                lvlData, tileSize);

        return leftCorner || rightCorner;
    }
}