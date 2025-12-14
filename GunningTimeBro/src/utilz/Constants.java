package utilz;

import main.Game;

public class Constants {

    // Enemy
    public static class EnemyConstants {
        public static final int CRABBY = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 40;

        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_type) {
                case CRABBY:
                    switch (enemy_state) {
                        case IDLE:
                            return 4;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 6;
                        case HIT:
                            return 2;
                        case DEAD:
                            return 5;
                    }
            }
            return 1;
        }
    }

    //Player
    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int HIT = 4;
        public static final int HITRUN = 5;
        public static final int LIGHTNING = 6;  // ✅ RENAMED from SUPERPOWER

        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case IDLE:
                    return 4;
                case RUNNING:
                    return 6;
                case HIT:
                case LIGHTNING:  // ✅ RENAMED
                    return 8;
                case JUMP:
                case FALLING:
                    return 4;
                case HITRUN:
                    return 6;
                default:
                    return 1;
            }
        }
    }
}