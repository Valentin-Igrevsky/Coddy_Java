import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;


class Game extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
    // Временное решения дял удобного размещения предметов
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean placementMode = false;
    private ArrayList<PlacementTool> tools = new ArrayList<>();
    private int currentToolIndex = 0;
    // Временное решения дял удобного размещения предметов

    private BufferedImage background;

    private final Player player;
//    private final KillZone killZone;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int cameraX = 0;
    private int cameraY = 0;

    private ArrayList<Platform> platforms = new ArrayList<>();
    private ArrayList<DamageZone> hazards = new ArrayList<>();
    private ArrayList<PhysicsDecoration> physicsDecorations = new ArrayList<>();
    private ArrayList<Decoration> decorations = new ArrayList<>();

    private GameState state = GameState.PLAYING;

    public Game() {
//        try {
//            background = javax.imageio.ImageIO.read(
//                    getClass().getResource("/background/background.png")
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ImageIcon icon = new ImageIcon(getClass().getResource("/background/background.png"));
//
//        int width = icon.getIconWidth();
//        int height = icon.getIconHeight();
//        setPreferredSize(new Dimension(width, height));
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(225, 149, 48));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        player = new Player(100, 400);
        platforms.add(new Platform(0, 500, "platform/2.png"));
//        killZone = new KillZone(-400, 650, 2000, 10);

        platforms.add(new Platform(523, 356, "platform/1.png"));
        physicsDecorations.add(new PhysicsDecoration(541, 285, "physical_decorations/Barrel Sprite.png"));
        physicsDecorations.add(new PhysicsDecoration(629, 386, "physical_decorations/barrel.png"));
        decorations.add(new Decoration(276, 382, "decorations/rocks.png"));
        hazards.add(new DamageZone(187, 462, "hazards/spike.png", 10));
        hazards.add(new DamageZone(220, 462, "hazards/spike.png", 10));
        hazards.add(new DamageZone(143, 467, "hazards/spike.png", 10));
        hazards.add(new DamageZone(682, 492, "hazards/lava.png", 50));
        platforms.add(new Platform(681, 543, "platform/2.png"));
        platforms.add(new Platform(863, 435, "platform/1.png"));

        initTools();

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

        for (PhysicsDecoration physicsDecoration : physicsDecorations) {
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

//        killZone.checkCollision(player);

        cameraX = (int) player.getX() - getWidth() / 2;
        cameraY = (int) player.getY() - getHeight() / 2;

        checkGameOver();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

//        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        g2d.translate(-cameraX, -cameraY);

        g2d.setColor(new Color(0, 0, 0));
        for (Platform platform : platforms) {
            platform.draw(g2d);
        }

        for (Decoration decoration : decorations) {
            decoration.draw(g2d);
        }

        for (PhysicsDecoration physicsDecoration : physicsDecorations) {
            physicsDecoration.draw(g2d);
        }

        for (GameObject hazard : hazards) {
            hazard.draw(g2d);
        }

//        killZone.draw(g2d);

        player.draw(g2d);

        if (state == GameState.GAME_OVER) {
            drawGameOver(g2d);
        }

        // Временное решения для удобного размещения предметов
        PlacementTool tool = getCurrentTool();

        if (placementMode && tool != null) {
            g2d = (Graphics2D) g.create();

            int worldX = mouseX + cameraX;
            int worldY = mouseY + cameraY;

            GameObject preview = tool.factory.apply(worldX, worldY);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            preview.draw(g2d);

            g2d.dispose();
        }
        // Временное решения для удобного размещения предметов
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
        // Временное решения для удобного размещения предметов
        if (placementMode) {
            if (keyCode == KeyEvent.VK_RIGHT) {
                currentToolIndex = (currentToolIndex + 1) % tools.size();
            }

            if (keyCode == KeyEvent.VK_LEFT) {
                currentToolIndex--;
                if (currentToolIndex < 0) currentToolIndex = tools.size() - 1;
            }
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            startPlacing();
        }
        // Временное решения для удобного размещения предметов
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_A) leftPressed = false;
        if (keyCode == KeyEvent.VK_D) rightPressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }


    @Override
    public void mousePressed(MouseEvent e) {
        // Временное решения для удобного размещения предметов
        PlacementTool tool = getCurrentTool();
        if (!placementMode || tool == null) return;

        int worldX = mouseX + cameraX;
        int worldY = mouseY + cameraY;

        System.out.printf(tool.pattern, worldX, worldY);

        GameObject obj = tool.factory.apply(worldX, worldY);
        tool.placer.accept(obj);
        // Временное решения для удобного размещения предметов
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

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
        g2d.translate(cameraX, cameraY);

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));

        String text = "GAME OVER";

        FontMetrics fm = g2d.getFontMetrics();

        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2;

        g2d.drawString(text, x, y);

        g2d.translate(-cameraX, -cameraY);
    }

    // Временное решения для удобного размещения предметов
    private void startPlacing() {
        placementMode = !placementMode;
    }

    private void loadToolsFromFolder(String folder, String type) {
        try {
            File dir = new File(getClass().getResource("/" + folder).toURI());

            for (File file : dir.listFiles()) {
                String path = folder + "/" + file.getName();

                switch (type) {
                    case "platform" -> tools.add(new PlacementTool(
                            (x, y) -> new Platform(x, y, path),
                            obj -> platforms.add((Platform) obj),
                            String.format("platforms.add(new Platform(%%s, %%s, \"%s\"));\n", path),
                            file.getName()
                    ));

                    case "hazards" -> tools.add(new PlacementTool(
                            (x, y) -> new DamageZone(x, y, path, 10),
                            obj -> hazards.add((DamageZone) obj),
                            String.format("hazards.add(new DamageZone(%%s, %%s, \"%s\", 50));\n", path),
                            file.getName()
                    ));

                    case "physical_decorations" -> tools.add(new PlacementTool(
                            (x, y) -> new PhysicsDecoration(x, y, path),
                            obj -> physicsDecorations.add((PhysicsDecoration) obj),
                            String.format("physicsDecorations.add(new PhysicsDecoration(%%s, %%s, \"%s\"));\n", path),
                            file.getName()
                    ));

                    case "decorations" -> tools.add(new PlacementTool(
                            (x, y) -> new Decoration(x, y, path),
                            obj -> decorations.add((Decoration) obj),
                            String.format("decorations.add(new Decoration(%%s, %%s, \"%s\"));\n", path),
                            file.getName()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTools() {
        loadToolsFromFolder("platform", "platform");
        loadToolsFromFolder("hazards", "hazards");
        loadToolsFromFolder("physical_decorations", "physical_decorations");
        loadToolsFromFolder("decorations", "decorations");
    }

    private PlacementTool getCurrentTool() {
        if (tools.isEmpty()) return null;
        return tools.get(currentToolIndex);
    }
    // Временное решения для удобного размещения предметов
}