import java.awt.*;

class WinTrigger extends InvisibleObject implements Trigger {
    public WinTrigger(double x, double y,int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void activate(GameObject obj, Game game) {
        game.win();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GREEN);
        super.draw(g);
    }
}