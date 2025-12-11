package utilz;

import java.awt.geom.Rectangle2D;
import main.Game;

@Deprecated
public class HelpMethods {


    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        return PhysicsHelper.canMoveHere(x, y, width, height, lvlData, Game. TILES_SIZE);
    }


    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        return PhysicsHelper.isSolid(x, y, lvlData, Game.TILES_SIZE);
    }


    public static float GetEntityXPosNextToWall(Rectangle2D. Float hitbox, float xSpeed) {
        return PhysicsHelper.getEntityXPosNextToWall(hitbox, xSpeed, Game. TILES_SIZE);
    }


    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        return PhysicsHelper.getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed, Game.TILES_SIZE);
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        return PhysicsHelper.isEntityOnFloor(hitbox, lvlData, Game.TILES_SIZE);
    }
}