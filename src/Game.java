import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

class Game extends JPanel implements KeyListener, ActionListener {
    private Player player;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private final ArrayList<Rectangle> platforms = new ArrayList<>();

    public Game() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(225, 149, 48));
        setFocusable(true);
        addKeyListener(this);
        player = new Player(100, 400);

        platforms.add(new Rectangle(0, 500, 2000, 100));
        platforms.add(new Rectangle(300, 420, 120, 20));
        platforms.add(new Rectangle(500, 350, 120, 20));
        platforms.add(new Rectangle(750, 300, 120, 20));

        Timer timer = new Timer(16, this);
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    private void update() {
        player.applyGravity();

        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();
        if (!leftPressed && !rightPressed) player.stop();

        player.update();

        for (Rectangle platform : platforms) {
            player.checkCollision(platform);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(0, 0, 0));
        for (Rectangle platform : platforms) {
            g2d.fill(platform);
        }

        player.draw(g2d);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_A) leftPressed = true;
        if (keyCode == KeyEvent.VK_D) rightPressed = true;
        if (keyCode == KeyEvent.VK_SPACE) player.jump();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_A) leftPressed = false;
        if (keyCode == KeyEvent.VK_D) rightPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}