package sound;

public interface SoundEffect {
    void play();
    void stop();
    void close();
    boolean isPlaying();
}
