class LevelExit extends InvisibleObject implements Trigger {
    public LevelExit(double x, double y,int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void activate(GameObject obj, Game game) {
        game.nextLevel();
    }
}