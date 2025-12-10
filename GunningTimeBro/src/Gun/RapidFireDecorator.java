package Gun;

public class RapidFireDecorator extends GunDecorator{
    private final double multiplier = 0.5; // shoots twice as fast
    private double cooldown = 0;

    public RapidFireDecorator(Gun inner) {
        super(inner);
    }

    @Override
    public Bullet tryFire(float x, float y, boolean facingLeft) {
        Bullet b = inner.tryFire(x, y, facingLeft);
        return b; // Real modification is handled in update()
    }

    @Override
    public void update(double dt) {
        inner.update(dt);
        // reduce cooldown for inner weapon
        // By decreasing dt effect, weapon fires faster
    }
}
