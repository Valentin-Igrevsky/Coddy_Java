public class TestObject extends Decoration implements Trigger {
    public TestObject(int x, int y) {
        super(x, y, "TestObject");
    }
    @Override
    public void activate(GameObject obj, Game game) {
        if (obj instanceof Player player) {
            player.damage(1000);
        }
    }
}
