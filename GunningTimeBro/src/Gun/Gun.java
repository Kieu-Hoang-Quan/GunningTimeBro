package Gun;

public interface Gun {
    void update(double dt);
    Bullet tryFire(float originX, float originY, boolean facingLeft);
    String getId();
}
