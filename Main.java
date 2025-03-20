import javax.swing.JFrame;  // Add this line

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Shooter");
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
