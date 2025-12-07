package sound;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.File;
public class WavSoundFactory implements SoundFactory {
    @Override
    public SoundEffect create(String filePath) throws UnsupportedAudioFileException, LineUnavailableException, IOException{
        File file = new File(filePath);
        if (!file.exists()) throw new IOException(" Sound File doesn't exist" + file.getAbsolutePath());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat audioFormat = audioInputStream.getFormat();
        AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                audioFormat.getSampleRate(),
                16,
                audioFormat.getChannels(),
                audioFormat.getChannels() * 2,
                audioFormat.getSampleRate(), false);
        AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, decodedFormat));
        clip.open(audioInputStream2);
        try {audioInputStream2.close();} catch (IOException ignored) {}
        try {audioInputStream.close();} catch (IOException ignored) {}
        return new WavSound(clip);
    }
    private static class WavSound implements SoundEffect {
        private final Clip clip;

        WavSound(Clip clip) {
            this.clip = clip;
        }

        @Override
        public synchronized void play() {
            if (clip == null) return;
            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }

        @Override
        public synchronized void stop() {
            if (clip == null) return;
            clip.stop();
        }

        @Override
        public synchronized void close() {
            if (clip == null) return;
            clip.stop();
            clip.close();
        }

        @Override
        public synchronized boolean isPlaying() {
            return clip != null && clip.isActive();
        }
    }
}
