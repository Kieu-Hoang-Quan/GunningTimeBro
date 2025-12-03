package gamestates;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {

    public static boolean[] keys = new boolean[256];
    public static final int ENTER = KeyEvent.VK_ENTER;
    public static final int ESC = KeyEvent.VK_ESCAPE;

    public static boolean isKeyPressed(int key) {
        return keys[key];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}