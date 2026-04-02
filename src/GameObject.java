import java.awt.*;

class GameObject {
    protected double x;
    protected double y;

    protected static final int PIXEL_SIZE = 4;

    protected Color[][] SPRITE;

    protected CollisionLayer layer;

    public GameObject(double x, double y, CollisionLayer layer) {
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    public CollisionLayer getLayer() {
        return layer;
    }

    public Rectangle getBounds() {
        int width = SPRITE[0].length * PIXEL_SIZE;
        int height = SPRITE.length * PIXEL_SIZE;

        return new Rectangle((int)x, (int)y, width, height);
    }

    public void draw(Graphics2D g2d) {
        for (int row = 0; row < SPRITE.length; row++) {
            for (int col = 0; col < SPRITE[row].length; col++) {
                if (SPRITE[row][col] != null) {
                    g2d.setColor(SPRITE[row][col]);
                    g2d.fillRect(
                            (int) x + col * PIXEL_SIZE,
                            (int) y + row * PIXEL_SIZE,
                            PIXEL_SIZE,
                            PIXEL_SIZE
                    );
                }
            }
        }
    }
}