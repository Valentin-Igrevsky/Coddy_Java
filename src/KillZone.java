public class KillZone extends InvisibleObject implements Trigger {
    public KillZone(double x, double y,int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void activate(GameObject obj, Game game) {
        if (obj instanceof Damageable target) {
            target.damage(100000);
        }
    }
}