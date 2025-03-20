import java.awt.*;
import javax.swing.*;

class Bullet {
    private int x, y;
    private final int speed = 10;
    private Image bulletImage;

    public Bullet(int x, int y) {
        this.x = x + 22; // Centering bullet
        this.y = y;
        loadImage();
    }

    private void loadImage() {
        bulletImage = new ImageIcon("assets/missile.png").getImage();
    }

    public boolean move() {
        y -= speed;
        return y > 0;
    }

    public void draw(Graphics g) {
        g.drawImage(bulletImage, x, y, 10, 20, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 20);
    }
}
