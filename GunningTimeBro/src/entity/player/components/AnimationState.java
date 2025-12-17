package entity.player.components;

import static utilz.Constants.PlayerConstants.*;

public class AnimationState {

    private MovementState movement;
    private boolean attacking;
    private boolean usingLightning;

    public AnimationState(MovementState movement) {
        this.movement = movement;
        this.attacking = false;
        this.usingLightning = false;
    }

    public int getCurrentAction() {
        if (usingLightning) {
            return LIGHTNING;
        }

        if (attacking && movement.isMoving() && ! movement.isInAir()) {
            return HITRUN;
        }

        if (attacking) {
            return HIT;
        }

        if (movement.isInAir()) {
            if (movement.getAirSpeed() < 0) {
                return JUMP;
            } else {
                return FALLING;
            }
        }

        if (movement.isMoving()) {
            return RUNNING;
        }

        return IDLE;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setUsingLightning(boolean usingLightning) {
        this.usingLightning = usingLightning;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean isUsingLightning() {
        return usingLightning;
    }
}