package main;

import java.awt. event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing. JFrame;

public class GameWindow {

    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {
        System.out.println("[GameWindow] Creating JFrame.. .");
        jframe = new JFrame();

        System.out.println("[GameWindow] Configuring frame...");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null);
        jframe.setResizable(false);

        System.out.println("[GameWindow] Packing frame...");
        jframe.pack();

        System.out.println("[GameWindow] Making frame visible...");
        jframe.setVisible(true);
        System.out.println("[GameWindow] Frame should be visible now!");

        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) { }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });

        System.out.println("[GameWindow] Window created successfully ✓");
    }
}