package main;

import javax.swing.*;

public class Main {
    public static JFrame window;

    public static void main(String[] args) {
        System.out.println("[Main] Starting game...");

        try {
            new Game();
            System.out.println("[Main] Game object created successfully");
        } catch (Exception e) {
            System.err.println("[Main] ========== FATAL ERROR ==========");
            System.err.println("[Main] Game failed to start!");
            e.printStackTrace();
            System.err.println("[Main] ====================================");
            System.exit(1);
        }

        System.out.println("[Main] Main method finished");
    }
}