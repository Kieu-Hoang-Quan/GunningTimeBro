package sound;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.*;

public class SoundPool {
    private final SoundFactory factory;
    private final Map<String, List<SoundEffect>> pool = new HashMap<>();
    public SoundPool(SoundFactory factory) {
        this.factory = factory;
    }
    public void preload(String key, String path, int count) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(path);
        int clones = Math.max(1, count);

        List<SoundEffect> list = pool.computeIfAbsent(key, k -> new ArrayList<>());
        for (int i = 0; i < clones; i++) {
            try {
                SoundEffect s = factory.create(path);
                list.add(s);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                System.err.println("[SoundPool] preload failed for " + path + ": " + e.getMessage());
            }
        }
    }
    public void play(String key) {
        List<SoundEffect> list = pool.get(key);
        if (list == null || list.isEmpty()) {
            System.err.println("[SoundPool] no sound preloaded for key=" + key);
            return;
        }

        // find an available instance (not playing)
        for (SoundEffect s : list) {
            if (!s.isPlaying()) {
                s.play();
                return;
            }
        }
        SoundEffect s0 = list.get(0);
        s0.play();
    }
    public void closeAll() {
        for (List<SoundEffect> list : pool.values()) {
            for (SoundEffect s : list) {
                try {
                    s.close();
                } catch (Exception ignored) {}
            }
        }
        pool.clear();
    }
}
