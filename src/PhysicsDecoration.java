class PhysicsDecoration extends PhysicsObject {
    public PhysicsDecoration(double x, double y, String spritePath) {
        super(x, y);
        this.SPRITE = SpriteLoader.loadSprite(spritePath);
    }
}


