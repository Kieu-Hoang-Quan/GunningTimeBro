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



    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        // Vị trí để kiểm tra nền
        float xCheck = 0;
        // Tăng vùng kiểm tra Y để ổn định (giống buffer = 3)
        int yCheck = (int) (hitbox.y + hitbox.height + 3); // <--- Tăng từ +1 lên +3

        if (xSpeed > 0) {
            // Đi sang phải, kiểm tra góc phải dưới
            xCheck = hitbox.x + hitbox.width + xSpeed;
        } else {
            // Đi sang trái, kiểm tra góc trái dưới
            xCheck = hitbox.x + xSpeed;
        }

        return IsSolid(xCheck, yCheck, lvlData);
    }


    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);

        if (xSpeed > 0) {
            // Đụng Tường Phải:
            int tileXPos = (currentTile + 1) * Game.TILES_SIZE;

            // Trả về: (Mép trái tile tiếp theo) - (Chiều rộng hitbox) - 1 pixel an toàn
            return tileXPos - hitbox.width - 1; // <--- Đảm bảo có -1 pixel an toàn

        } else {
            // Đụng Tường Trái:
            return currentTile * Game.TILES_SIZE;
        }
    }


    // Trong HelpMethods.java

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);

        // Tăng pixel an toàn lên -2 để đảm bảo không bị kẹt
        int safe_margin = 2; // <--- Tăng từ 1 lên 2

        if (airSpeed > 0) {
// Falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            // Tính toán vị trí Y mà hitbox nằm ngay trên mép tile
            int yPosOnTile = (int) (tileYPos + Game.TILES_SIZE - hitbox.height);

            // Trả về vị trí Y được căn chỉnh, trừ đi pixel an toàn
            return yPosOnTile - safe_margin; // <--- Sử dụng safe_margin = 2

        } else {
            // Jumping - hitting ceiling (Giữ nguyên)
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Tăng vùng kiểm tra từ +1 lên +3 để chống lỗi làm tròn (Flickering)
        int buffer = 3;

        // Kiểm tra góc dưới bên trái của hitbox
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + buffer, lvlData))

            // Kiểm tra góc dưới bên phải của hitbox
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + buffer, lvlData))
                return false; // Nếu cả hai góc đều không solid, thì không trên sàn

        return true; // Ngược lại, ít nhất một góc có solid, thì trên sàn
    }
}