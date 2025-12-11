package utilz;

public class Constants {

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int HIT = 6;

        @Deprecated
        public static int GetSpriteAmount(int player_action) {
            return GameConfig.PlayerAnimation.getSpriteAmount(player_action);
        }
    }
}