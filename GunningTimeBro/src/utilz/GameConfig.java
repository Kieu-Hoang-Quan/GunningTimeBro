package utilz;

public class GameConfig {
    public enum PlayerAnimation {
        IDLE(0, 4),
        RUNNING(1, 6),
        JUMP(2, 4),
        FALLING(3, 4),
        GROUND(4, 1),
        HIT(6, 6),
        ATTACK_1(6, 6),
        ATTACK_JUMP_1(7, 3),
        ATTACK_JUMP_2(8, 3);

        private final int state;
        private final int spriteCount;

        PlayerAnimation(int state, int spriteCount) {
            this.state = state;
            this.spriteCount = spriteCount;
        }

        public static int getSpriteAmount(int playerAction) {
            for (PlayerAnimation anim : values()) {
                if (anim.state == playerAction) {
                    return anim.spriteCount;
                }
            }
            return 1;
        }
    }
}