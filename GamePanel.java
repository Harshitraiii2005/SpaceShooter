import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private javax.swing.Timer gameTimer;
    private javax.swing.Timer enemySpawnTimer;
    private int score;
    private boolean gameOver;
    private boolean restartMessageVisible = true;
    private Image background;
    private int bgY = 0;
    private Clip backgroundMusic;
    private long lastShotTime = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();

        player = new Player();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        gameTimer = new javax.swing.Timer(30, this);
        gameTimer.start();

        background = new ImageIcon("assets/backgrounds.jpg").getImage();

        playMusic("assets/background.wav");

        enemySpawnTimer = new javax.swing.Timer(1500, e -> spawnEnemy());
        enemySpawnTimer.start();

        startBlinkingRestartMessage();
    }

    private void playMusic(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void spawnEnemy() {
        Random rand = new Random();
        enemies.add(new Enemy(rand.nextInt(750), -50));
    }

    private void startBlinkingRestartMessage() {
        new javax.swing.Timer(500, e -> restartMessageVisible = !restartMessageVisible).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Background scrolling
        bgY += 2;
        if (bgY > getHeight()) bgY = 0;
        g.drawImage(background, 0, bgY - getHeight(), getWidth(), getHeight(), this);
        g.drawImage(background, 0, bgY, getWidth(), getHeight(), this);

        player.draw(g);

        for (Bullet bullet : bullets) bullet.draw(g);
        for (Enemy enemy : enemies) enemy.draw(g);

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, getWidth() - 120, 30);

        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 250, 300);

            if (restartMessageVisible) {
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.setColor(Color.YELLOW);
                g.drawString("Press 'R' to Restart", 250, 350);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            player.move();
            bullets.removeIf(b -> !b.move());
            enemies.removeIf(enemy -> {
                if (!enemy.move()) {
                    gameOver = true;
                    gameTimer.stop();
                    enemySpawnTimer.stop();
                    return true;
                }
                return false;
            });

            for (Iterator<Bullet> bulletIt = bullets.iterator(); bulletIt.hasNext(); ) {
                Bullet bullet = bulletIt.next();
                for (Iterator<Enemy> enemyIt = enemies.iterator(); enemyIt.hasNext(); ) {
                    Enemy enemy = enemyIt.next();
                    if (bullet.getBounds().intersects(enemy.getBounds())) {
                        bulletIt.remove();
                        enemyIt.remove();
                        score += 10;
                        playSound("assets/explosion.wav");
                        break;
                    }
                }
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            player.keyPressed(e);
            if (e.getKeyCode() == KeyEvent.VK_SPACE && System.currentTimeMillis() - lastShotTime > 300) {
                bullets.add(new Bullet(player.getX(), player.getY()));
                playSound("assets/shoot.wav");
                lastShotTime = System.currentTimeMillis();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }

    private void restartGame() {
        gameOver = false;
        score = 0;
        player = new Player();
        bullets.clear();
        enemies.clear();
        gameTimer.start();
        enemySpawnTimer.start();
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) { player.keyReleased(e); }
    @Override public void keyTyped(KeyEvent e) {}
}
