import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

class Game extends JPanel implements KeyListener, ActionListener, MouseListener {
    private Player player;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int cameraX = 0;
    private int cameraY = 0;

    private final ArrayList<Platform> platforms = new ArrayList<>();

    public Game() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(225, 149, 48));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        player = new Player(100, 400);

        platforms.add(new Platform(0, 500, "platform/2.png"));
        platforms.add(new Platform(300, 420, "platform/1.png"));
        platforms.add(new Platform(500, 350, "platform/1.png"));
        platforms.add(new Platform(750, 300, "platform/1.png"));
        platforms.add(new Platform(800, 400, "platform/1.png"));

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

        for (Platform platform : platforms) {
            player.checkCollision(platform.getBounds());
        }

        cameraX = (int) player.getX() - getWidth() / 2;
        cameraY = (int) player.getY() - getHeight() / 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(-cameraX, -cameraY);

        g2d.setColor(new Color(0, 0, 0));
        for (Platform platform : platforms) {
            platform.draw(g2d);
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
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {

            int worldX = e.getX() + cameraX;
            int worldY = e.getY() + cameraY;

            platforms.add(new Platform(worldX, worldY, "platform/1.png"));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}