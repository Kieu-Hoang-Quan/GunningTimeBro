package entity.player.playercomponents;

import entity.player.playercomponents.MovementState;

import static utilz.Constants.PlayerConstants.*;
public class AnimationState {

    private MovementState movement;
    private boolean attacking;

    public AnimationState(MovementState movement) {
        this.movement = movement;
        this.attacking = false;
    }


    public int getCurrentAction() {
        // Priority 1: Attacking
        if (attacking) {
            return HIT;
        }

        // Priority 2: In Air
        if (movement.isInAir()) {
            if (movement.getAirSpeed() < 0) {
                return JUMP;  // Going up
            } else {
                return FALLING;  // Going down
            }
        }

        // Priority 3: Moving
        if (movement.isMoving()) {
            return RUNNING;
        }

        // Priority 4: Idle
        return IDLE;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isAttacking() {
        return attacking;
    }
}