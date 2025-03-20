import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

class Player {
    private int x, y;
    private Image planeImage;
    private boolean leftPressed = false, rightPressed = false;

    public Player() {
        this.x = 360; // Centered start position
        this.y = 500; // Near bottom
        this.planeImage = new ImageIcon("assets/player.png").getImage();
    }

    public void draw(Graphics g) {
        g.drawImage(planeImage, x, y, 80, 80, null);
    }

    public void move() {
        if (leftPressed && x > 0) x -= 10;
        if (rightPressed && x < 720) x += 10;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
