package Gun;
import sound.SoundPool;
public class BaseGun implements Gun{
    private final SoundPool soundPool;
    private final String id;

    private int damage = 1;
    private float bulletSpeed = 7f;
    private double cooldown = 0;
    private double fireRate = 0.25;

    public BaseGun(String id, SoundPool pool) {
        this.id = id;
        this.soundPool = pool;
    }

    @Override
    public void update(double dt) {
        if (cooldown > 0) cooldown -= dt;
    }

    @Override
    public Bullet tryFire(float x, float y, boolean facingLeft) {
        if (cooldown > 0) return null;
        cooldown = fireRate;

        float dir = facingLeft ? -1 : 1;
        soundPool.play("gunShot");

        return new Bullet(x, y, bulletSpeed * dir);
    }

    @Override
    public String getId() {
        return id;
    }
}
