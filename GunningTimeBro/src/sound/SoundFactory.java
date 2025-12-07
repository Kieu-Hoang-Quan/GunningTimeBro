package sound;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
public interface SoundFactory {
    SoundEffect create(String filePath) throws UnsupportedAudioFileException, LineUnavailableException, IOException;
}

