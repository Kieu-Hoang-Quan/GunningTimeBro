package sound;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.Objects;

public class SoundPlayer {

    private Clip clip;

    public void BackgroundMusic() {
        try {
            // 1) Tìm file trong resources
            String path = "/sound/background.wav";
            InputStream is = SoundPlayer.class.getResourceAsStream(path);

            if (is == null) {
                System.out.println("[SOUND] NOT FOUND: " + path);
                return;
            }

            // 2) Load âm thanh
            AudioInputStream audio = AudioSystem.getAudioInputStream(is);
            clip = AudioSystem.getClip();
            clip.open(audio);

            // 3) Play loop
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            System.out.println("[SOUND] Background started.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}