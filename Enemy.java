import java.awt.*;
import javax.swing.*;

class Enemy {
    private int x, y;
    private final int speed = 3;
    private Image enemyImage;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        loadImage();
    }

    private void loadImage() {
        enemyImage = new ImageIcon("assets/enemy.png").getImage();
    }

    public boolean move() {
        y += speed;
        return y < 600; // If enemy moves out of bounds, return false (remove it)
    }

    public void draw(Graphics g) {
        g.drawImage(enemyImage, x, y, 50, 50, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }
}
