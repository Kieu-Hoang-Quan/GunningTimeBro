package entity.player.playercomponents;

import entity.player.PlayerPhysics;

public class MovementState {

    private PlayerPhysics physics;
    private InputHandler input;

    public MovementState(PlayerPhysics physics, InputHandler input) {
        this.physics = physics;
        this.input = input;
    }


    public boolean isMoving() {
        return input.isLeft() || input.isRight();
    }


    public boolean isInAir() {
        return physics.isInAir();
    }


    public float getAirSpeed() {
        return physics.getAirSpeed();
    }


    public boolean shouldFlipLeft() {
        return input.isLeft();
    }


    public boolean shouldFlipRight() {
        return input.isRight();
    }
}
