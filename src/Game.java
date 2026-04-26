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
    private final int gridSize;
    private boolean placementMode = false;
    private ArrayList<PlacementTool> tools = new ArrayList<>();
    private int currentToolIndex = 0;
    // Временное решения дял удобного размещения предметов

    private BufferedImage background;

    private Level currentLevel;
    private int currentLevelIndex = 1;
    private final Player player;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private int cameraX = 0;
    private int cameraY = 0;

    private ArrayList<Platform> platforms;
    private ArrayList<DamageZone> hazards;
    private ArrayList<PhysicsDecoration> physicsDecorations;
    private ArrayList<Decoration> decorations;
    private ArrayList<Trigger> triggers;

    private GameState state = GameState.PLAYING;

    public Game() {
        try {
            background = javax.imageio.ImageIO.read(
                    getClass().getResource("/background/background.png")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        int width = background.getWidth();
        int height = background.getHeight();
        setPreferredSize(new Dimension(width, height));

        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        gridSize = GameObject.PIXEL_SIZE;

        player = new Player(0, 0);
        loadLevel(currentLevelIndex);

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

        if (!placementMode) player.update();

        for (PhysicsDecoration physicsDecoration : physicsDecorations) {
            physicsDecoration.update();
        }

        for (DamageZone hazard : hazards) {
            hazard.update();
        }

        for (Platform platform : platforms) {
            if (!placementMode) checkCollision(player, platform);

            for (PhysicsDecoration physicsDecoration : physicsDecorations) {
                checkCollision(physicsDecoration, platform);
                if (!placementMode) checkCollision(physicsDecoration, player);

                for (PhysicsDecoration physicsDecoration2 : physicsDecorations) {
                    if (physicsDecoration2 != physicsDecoration) {
                        checkCollision(physicsDecoration2, physicsDecoration);
                    }
                }
            }
        }

        for (DamageZone hazard : hazards) {
            if (!placementMode) hazard.checkCollision(player);
        }

        for (Trigger trigger : triggers) {
            if (player.getBounds().intersects(((GameObject) trigger).getBounds())) {
                if (!placementMode) trigger.activate(player, this);
            }
        }

        cameraX = (int) player.getX() - getWidth() / 2;
        cameraY = (int) player.getY() - getHeight() / 2;

        checkGameOver();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Временное решения для удобного размещения предмето
        if (placementMode) {
            drawGrid(g2d);
        } else {
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
        // Временное решения для удобного размещения предмето

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

        if (placementMode) {
            for (Trigger trigger : triggers) {
                ((GameObject) trigger).draw(g2d);
            }
        }

        player.draw(g2d);

        if (state == GameState.GAME_OVER) {
            drawGameOver(g2d);
        }

        // Временное решения для удобного размещения предмето
        PlacementTool tool = getCurrentTool();

        if (placementMode && tool != null) {
            g2d = (Graphics2D) g.create();

            int worldX = (mouseX + cameraX)/gridSize;
            int worldY = (mouseY + cameraY)/gridSize;

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

        if (placementMode) {
            switch (keyCode) {
                case KeyEvent.VK_A -> player.setX(player.getX() - gridSize);
                case KeyEvent.VK_D -> player.setX(player.getX() + gridSize);
                case KeyEvent.VK_S -> player.setY(player.getY() + gridSize);
                case KeyEvent.VK_W -> player.setY(player.getY() - gridSize);
            }
        } else {
            if (keyCode == KeyEvent.VK_A) leftPressed = true;
            if (keyCode == KeyEvent.VK_D) rightPressed = true;
            if (keyCode == KeyEvent.VK_SPACE) player.jump();
        }

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

        int worldX = (mouseX + cameraX)/gridSize;
        int worldY = (mouseY + cameraY)/gridSize;

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
                            String.format("level.platforms.add(new Platform(%%s, %%s, \"%s\"));\n", path),
                            file.getName()
                    ));

                    case "hazards" -> tools.add(new PlacementTool(
                            (x, y) -> new DamageZone(x, y, path, 10),
                            obj -> hazards.add((DamageZone) obj),
                            String.format("level.hazards.add(new DamageZone(%%s, %%s, \"%s\", 50));\n", path),
                            file.getName()
                    ));

                    case "physical_decorations" -> tools.add(new PlacementTool(
                            (x, y) -> new PhysicsDecoration(x, y, path),
                            obj -> physicsDecorations.add((PhysicsDecoration) obj),
                            String.format("level.physicsDecorations.add(new PhysicsDecoration(%%s, %%s, \"%s\"));\n", path),
                            file.getName()
                    ));

                    case "decorations" -> tools.add(new PlacementTool(
                            (x, y) -> new Decoration(x, y, path),
                            obj -> decorations.add((Decoration) obj),
                            String.format("level.decorations.add(new Decoration(%%s, %%s, \"%s\"));\n", path),
                            file.getName()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTriggerTools() {
        tools.add(new PlacementTool(
                (x, y) -> new KillZone(x, y, 50, 10),
                obj -> triggers.add((Trigger) obj),
                "level.triggers.add(new KillZone(%s, %s, 50, 10));\n",
                "KillZone"
        ));

        tools.add(new PlacementTool(
                (x, y) -> new LevelExit(x, y, 10, 10),
                obj -> triggers.add((Trigger) obj),
                "level.triggers.add(new LevelExit(%s, %s, 10, 10));\n",
                "LevelExit"
        ));
    }


    private void initTools() {
        loadToolsFromFolder("platform", "platform");
        loadToolsFromFolder("hazards", "hazards");
        loadToolsFromFolder("physical_decorations", "physical_decorations");
        loadToolsFromFolder("decorations", "decorations");
        loadTriggerTools();
    }

    private PlacementTool getCurrentTool() {
        if (tools.isEmpty()) return null;
        return tools.get(currentToolIndex);
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 40));

        int width = getWidth();
        int height = getHeight();

        int startX = cameraX;
        int startY = cameraY;

        int endX = cameraX + width;
        int endY = cameraY + height;

        startX = (startX / gridSize) * gridSize;
        startY = (startY / gridSize) * gridSize;

        // вертикальные линии
        for (int x = startX; x <= endX; x += gridSize) {
            int screenX = x - cameraX;
            g2d.drawLine(screenX, 0, screenX, height);
        }

        // горизонтальные линии
        for (int y = startY; y <= endY; y += gridSize) {
            int screenY = y - cameraY;
            g2d.drawLine(0, screenY, width, screenY);
        }
    }
    // Временное решения для удобного размещения предметов



    public void loadLevel(int id) {
        currentLevel = LevelLoader.load(id);

        player.setX(0);
        player.setY(0);

        platforms = currentLevel.platforms;
        hazards = currentLevel.hazards;
        physicsDecorations = currentLevel.physicsDecorations;
        decorations = currentLevel.decorations;
        triggers = currentLevel.triggers;

        cameraX = 0;
        cameraY = 0;
    }

    public void nextLevel() {
        currentLevelIndex += 1;
        loadLevel(currentLevelIndex);
    }
}