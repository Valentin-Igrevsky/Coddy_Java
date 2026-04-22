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
    private final Player player;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int cameraX = 0;
    private int cameraY = 0;

    private final ArrayList<Platform> platforms = new ArrayList<>();
    private final ArrayList<DamageZone> hazards = new ArrayList<>();
    private final ArrayList<PhysicsDecoration> physicsDecorations = new ArrayList<>();

    private GameState state = GameState.PLAYING;

    public Game() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(225, 149, 48));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        player = new Player(100, 400);

        platforms.add(new Platform(0, 500, "platform/2.png"));
        platforms.add(new Platform(500, 500, "platform/2.png"));
        platforms.add(new Platform(100, 400, "platform/1.png"));
        platforms.add(new Platform(300, 350, "platform/1.png"));
        platforms.add(new Platform(500, 350, "platform/1.png"));
        platforms.add(new Platform(650, 300, "platform/1.png"));
        platforms.add(new Platform(800, 400, "platform/3.png"));

        hazards.add(new DamageZone(200, 470, "hazards/spike.png", 10));
        hazards.add(new DamageZone(240, 470, "hazards/spike.png", 10));
        hazards.add(new DamageZone(280, 460, "hazards/spike.png", 10));

        physicsDecorations.add(new PhysicsDecoration(100, 0, "decorations/Barrel Sprite.png"));
        physicsDecorations.add(new PhysicsDecoration(100, -150, "decorations/Barrel Sprite.png"));

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
        if (state != GameState.PLAYING) return;

        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();
        if (!leftPressed && !rightPressed) player.stop();

        player.update();

        for (PhysicsDecoration physicsDecoration: physicsDecorations) {
            physicsDecoration.update();
        }

        for (DamageZone hazard : hazards) {
            hazard.update();
        }

        for (Platform platform : platforms) {
            checkCollision(player, platform);

            for (PhysicsDecoration physicsDecoration : physicsDecorations) {
                checkCollision(physicsDecoration, platform);
                checkCollision(physicsDecoration, player);

                for (PhysicsDecoration physicsDecoration2 : physicsDecorations) {
                    if (physicsDecoration2 != physicsDecoration) {
                        checkCollision(physicsDecoration2, physicsDecoration);
                    }
                }
            }
        }

        for (DamageZone hazard : hazards) {
            hazard.checkCollision(player);
        }

//        for (PhysicsDecoration physicsDecoration : physicsDecorations) {
//            physicsDecoration.checkCollision(player);
//        }

        cameraX = (int) player.getX() - getWidth() / 2;
        cameraY = (int) player.getY() - getHeight() / 2;

        checkGameOver();
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

        for (GameObject hazard : hazards) {
            hazard.draw(g2d);
        }

        for (PhysicsDecoration physicsDecoration : physicsDecorations) {
            physicsDecoration.draw(g2d);
        }

        player.draw(g2d);

        if (state == GameState.GAME_OVER) {
            drawGameOver(g2d);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_A) leftPressed = true;
        if (keyCode == KeyEvent.VK_D) rightPressed = true;
        if (keyCode == KeyEvent.VK_SPACE) player.jump();
        if (keyCode == KeyEvent.VK_P) {
            switch (state) {
                case GameState.PLAYING -> state = GameState.PAUSE;
                case GameState.PAUSE -> state = GameState.PLAYING;
            }
        }
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
            player.damage(10);
            System.out.println(player.getHealth());
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            player.heal(10);
            System.out.println(player.getHealth());
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

    public <A extends PhysicsObject, B extends GameObject> void checkCollision(A first, B second) {
        Rectangle firstBounds = first.getBounds();
        Rectangle secondBounds = second.getBounds();

        if (firstBounds.intersects(secondBounds)) {
            Rectangle intersection = firstBounds.intersection(secondBounds);

            if (intersection.height < intersection.width) {
                if (first instanceof PhysicsDecoration && second instanceof Player) {
                    if (first.y > second.y) {
                        second.y -= intersection.height;
                        ((Player) second).onGround = true;
                    } else {
                        first.y -= intersection.height;
                        first.setVelY(0);
                        first.onGround = true;
                    }
                    ((Player) second).velY = 0;
                } else {
                    if (first.y < second.y) {
                        first.y -= intersection.height;
                        first.onGround = true;
                    } else {
                        first.y += intersection.height;
                    }
                    first.velY = 0;
                }
            } else {
                if (first.x < second.x) {
                    first.x -= intersection.width;
                } else {
                    first.x += intersection.width;
                }
                first.velX = 0;
            }
        }
    }

    private void checkGameOver() {
        if (player.getHealth() <= 0) {
            state = GameState.GAME_OVER;
        }
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));

        String text = "GAME OVER";

        FontMetrics fm = g2d.getFontMetrics();
        int x = (800 - fm.stringWidth(text)) / 2;
        int y = 400;

        g2d.drawString(text, x, y);
    }
}