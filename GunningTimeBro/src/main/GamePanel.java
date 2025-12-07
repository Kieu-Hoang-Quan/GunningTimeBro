package main;

import java.awt.*;
import javax.swing.JPanel;
import sound.SoundFactory;
import sound.WavSoundFactory;
import sound.SoundPool;

import inputs.KeyboardInputs;
import sound.SoundFactory;
import sound.SoundPool;
import sound.WavSoundFactory;

public class GamePanel extends JPanel {

    private Game game;
    private final SoundPool soundPool;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();

        addKeyListener(new KeyboardInputs(this));
        setFocusable(true);
        requestFocusInWindow();

        SoundFactory soundFactory = new WavSoundFactory();
        soundPool = new SoundPool(soundFactory);

        // preload trên một thread để không block UI
        new Thread(() -> {
            try {
                // Chú ý đường dẫn: dùng đúng relative path so với working dir của chương trình
                // nếu bạn đặt file trong res/sound/ như trong project, dùng "res/sound/..."
                soundPool.preload("punch", "res/sound/punch-416719.wav", 4);
                soundPool.preload("gunShot", "res/sound/gun-shot-350315.wav", 6);
                System.out.println("[Sound] preloaded sounds");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "SoundPreload").start();
        addKeyListener(new KeyboardInputs(this));
        setFocusable(true);
        requestFocusInWindow();
    }

    private void setPanelSize() {
        setPreferredSize(new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g2) {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D) g2;

        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}


