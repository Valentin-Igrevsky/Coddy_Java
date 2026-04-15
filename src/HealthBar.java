import java.awt.*;

class HealthBar extends GameObject {
    private final GameObject owner;
    private final Damageable target;
    private final int width;
    private final int height = 6;
    private final int offsetY;

    public HealthBar(GameObject owner, Damageable target) {
        super(owner.getX(), owner.getY());
        this.owner = owner;
        this.target = target;
        this.width = owner.getBounds().width;
        this.offsetY = owner.getBounds().height + 5;
    }

    @Override
    public void draw(Graphics2D g2d) {
        // позиция относительно объекта
        this.x = owner.getX();
        this.y = owner.getY() + offsetY;
        int currentHealth = target.getHealth();
        int maxHealth = target.getMaxHealth();
        float percent = Math.max(0, currentHealth / (float) maxHealth);
        int currentWidth = (int) (width * percent);
        // фон
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect((int)x, (int)y, width, height);

        // цвет (градиент)
        Color color = new Color(1 - percent, percent, 0);
        g2d.setColor(color);

        // заполнение
        g2d.fillRect((int)x, (int)y, currentWidth, height);

        // рамка
        g2d.setColor(Color.BLACK);
        g2d.drawRect((int)x, (int)y, width, height);
    }
}