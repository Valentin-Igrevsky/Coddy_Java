import java.awt.*;

public class KillZone extends GameObject {
    int width;
    int height;

    public KillZone(double x, double y,int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public Rectangle getBounds() {
        int width = this.width * PIXEL_SIZE;
        int height = this.height * PIXEL_SIZE;

        return new Rectangle((int) x, (int) y, width, height);
    }

    public void checkCollision(GameObject obj) {
        if (this.getBounds().intersects(obj.getBounds())) {
            if (obj instanceof Damageable target) {
                target.damage(100000);
            }
        }
    }
}
