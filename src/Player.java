import java.awt.*;
import java.util.ArrayList;

class Player extends PhysicsObject {
    private final double jumpStrength = -12;
    private final double speedStrength = 4;

    private int health = 100;

    private final ArrayList<Color[][]> SPRITES;

    private int currentFrame = 0;
    private int tickFrame = 0;

    public Player(double x, double y) {
        super(x, y);
        SPRITES = SpriteLoader.loadSprites("player");
        SPRITE = SPRITES.getFirst();
    }

    private void updateAnimation() {
        if (velX != 0) {
            tickFrame++;
            if (tickFrame > 8) {
                currentFrame = (currentFrame + 1) % SPRITES.size();
                SPRITE = SPRITES.get(currentFrame);
                tickFrame = 0;
            }
        } else {
            currentFrame = 0;
            SPRITE = SPRITES.getFirst();
            tickFrame = 0;
        }
    }

    @Override
    public void update() {
        x += velX;
        velY += gravity;
        y += velY;
        updateAnimation();
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

    void damage(int damage) {
        health -= damage;
    }

    void heal(int heal) {
        health += heal;
    }

    int getHealth() {
        return health;
    }
}