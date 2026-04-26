import java.awt.*;
import java.util.ArrayList;

class Player extends PhysicsObject implements Damageable {
    private final double jumpStrength = -3;
    private final double speedStrength = 1;

    private int maxHealth = 100;
    private int health = 100;

    private final ArrayList<Color[][]> SPRITES;

    private int currentFrame = 0;
    private int tickFrame = 0;

    private final HealthBar healthBar;

    public Player(double x, double y) {
        super(x, y);
        SPRITES = SpriteLoader.loadSprites("player");
        SPRITE = SPRITES.getFirst();
        healthBar = new HealthBar(this, this);
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
        setVelX(-speedStrength);
    }

    public void moveRight() {
        setVelX(speedStrength);
    }

    public void stop() {
        velX = 0;
    }

    public void jump() {
        if (onGround) {
            setVelY(jumpStrength);
            setOnGround(false);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        healthBar.draw(g2d);
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) { this.health = health; }

    @Override
    public int getMaxHealth() { return maxHealth; }

    @Override
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    @Override
    public void damage(int damage) {
        if (health - damage >= 0) health -= damage;
        else health = 0;
    }

    @Override
    public void heal(int heal) {
        if (health + heal <= maxHealth) health += heal;
        else health = maxHealth;
    }
}