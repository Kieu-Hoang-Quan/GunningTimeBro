package Gun;

public abstract class GunDecorator implements Gun {
    protected final Gun inner;

    public GunDecorator(Gun inner) {
        this.inner = inner;
    }

    @Override
    public void update(double dt) {
        inner.update(dt);
    }

    @Override
    public String getId() {
        return inner.getId();
    }
}
