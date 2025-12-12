package utilz;

import main.Game;

public class Constants {

    public static class EnemyConstants{
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

        public static int GetSpriteAmount(int enemy_type, int enemy_state){
            switch(enemy_type){
                case CRABBY:
                    switch (enemy_state){
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
            return 0;
        }
    }
    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int HIT = 6;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_JUMP_1 = 7;
        public static final int ATTACK_JUMP_2 = 8;

        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case RUNNING:
                case HIT:
                    return 6;
                case IDLE:
//                case JUMP:
                    return 4;
                case JUMP:
                case FALLING:
                    return 4;
//                case ATTACK_1:
//                case ATTACK_JUMP_1:
//                case ATTACK_JUMP_2:
//                    return 3;
//                case GROUND:
//                    return 2;
//                case FALLING:
                default:
                    return 1;
            }
        }
    }
}