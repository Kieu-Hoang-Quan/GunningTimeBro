package Gun;
import sound.SoundPool;
public class GunFactory {
    public static Gun createBasicGun(SoundPool pool) {
        return new BaseGun("basic_gun", pool);
    }

    public static Gun createRapidFireGun(SoundPool pool) {
        return new RapidFireDecorator(new BaseGun("rapid_gun", pool));
    }
}
