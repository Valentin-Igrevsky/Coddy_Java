import java.awt.*;

class Platform {
    private double x;
    private double y;

    private static final int PIXEL_SIZE = 4;

    private final Color[][] SPRITE;

    private int currentFrame = 0;

    public Platform(double x, double y, String spritePath) {
        this.x = x;
        this.y = y;
        this.SPRITE = SpriteLoader.loadSprites(spritePath).getFirst();
    }

    public void draw(Graphics2D g2d) {
        for (int row = 0; row < this.SPRITE.length; row++) {
            for (int col = 0; col < this.SPRITE[row].length; col++) {

                if (this.SPRITE[row][col] != null) {
                    g2d.setColor(this.SPRITE[row][col]);

                    g2d.fillRect(
                            (int)x + col * PIXEL_SIZE,
                            (int)y + row * PIXEL_SIZE,
                            PIXEL_SIZE,
                            PIXEL_SIZE
                    );
                }
            }
        }
    }
}