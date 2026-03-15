import java.awt.*;
import java.util.ArrayList;

class Player {
    private double x;
    private double y;

    private double velX;
    private double velY;

    private final double jumpStrength = -12;
    private final double speedStrength = 4;

    private final double gravity = 0.5;

    private boolean onGround = true;

    private static final int PIXEL_SIZE = 4;

    private final ArrayList<Color[][]> SPRITES;

    private int currentFrame = 0;
    private int tickFrame = 0;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        SPRITES = SpriteLoader.loadSprites("player");
    }

    public void applyGravity() {
        velY += gravity;
    }

    public void update() {
        x += velX;
        y += velY;

        if (velX != 0) {
            tickFrame++;
            if (tickFrame > 8) {
                currentFrame = (currentFrame + 1) % SPRITES.size();
                tickFrame = 0;
            }
        } else {
            currentFrame = 0;
            tickFrame = 0;
        }
    }

    public void checkCollision(Rectangle platform) {
        Color[][] firstSprite = SPRITES.get(0);

        int width = firstSprite[0].length;
        int height = firstSprite.length;

        Rectangle playerBounds = new Rectangle(
                (int) x,
                (int) y,
                width * PIXEL_SIZE,
                height * PIXEL_SIZE
        );

        if (playerBounds.intersects(platform)) {
            Rectangle intersection = playerBounds.intersection(platform);

            if (intersection.height < intersection.width) {
                // Выше платформы
                if (y < platform.y) {
                    y -= intersection.height;
                    onGround = true;
                } else { // Ниже платформы
                    y += intersection.height;
                }
                velY = 0;
            } else {
                // Слева от платформы
                if (x < platform.x) {
                    x -= intersection.width;
                } else { // Справа от платформы
                    x += intersection.width;
                }
                velX = 0;
            }
        }
    }

    public void moveLeft() {
        velX = -speedStrength;
    }

    public void moveRight() {
        velX = speedStrength;
    }

    public void stop() {
        velX = 0;
    }

    public void jump() {
        if (onGround) {
            velY = jumpStrength;
            onGround = false;
        }
    }

    public void draw(Graphics2D g2d) {
        Color[][] sprite = SPRITES.get(currentFrame);
        for (int row = 0; row < sprite.length; row++) {
            for (int col = 0; col < sprite[row].length; col++) {
                if (sprite[row][col] != null) {
                    g2d.setColor(sprite[row][col]);
                    g2d.fillRect(
                            (int) x + col * PIXEL_SIZE,
                            (int) y + row * PIXEL_SIZE,
                            PIXEL_SIZE,
                            PIXEL_SIZE);
                }
            }
        }
    }
}