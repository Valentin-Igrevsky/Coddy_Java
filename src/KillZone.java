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

    @Override
    public void draw(Graphics2D g) {
        Rectangle bounds = getBounds();

        Stroke oldStroke = g.getStroke();
        float[] dashPattern = {10, 5};
        Stroke dashed = new BasicStroke(
                2,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10,
                dashPattern,
                0
        );

        g.setStroke(dashed);
        g.setColor(Color.RED);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        g.setStroke(oldStroke);
    }
}