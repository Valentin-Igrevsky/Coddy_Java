import java.awt.*;

class GameObject {
    protected double x;
    protected double y;

    protected final static int PIXEL_SIZE = 1;

    protected Color[][] SPRITE;

    public GameObject(double x, double y) {
        this.x = x*PIXEL_SIZE;
        this.y = y*PIXEL_SIZE;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Rectangle getBounds() {
        int width = SPRITE[0].length * PIXEL_SIZE;
        int height = SPRITE.length * PIXEL_SIZE;

        return new Rectangle((int) x, (int) y, width, height);
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