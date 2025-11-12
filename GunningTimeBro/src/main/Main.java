package main;

import javax.swing.*;

public class Main {
    public static JFrame window;
    public static void main(String[] args) {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false); // Cant Resizable
        window.setTitle("Gunning Time Bro!\n"); // Window Name
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); // Resizes to prefered size and prevents overflow.

        window.setLocationRelativeTo(null); // Starts center of screen
        window.setVisible(true);
    }
}