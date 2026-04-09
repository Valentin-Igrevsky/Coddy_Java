class Platform extends GameObject {
    public Platform(double x, double y, String spritePath) {
        super(x, y);
        this.SPRITE = SpriteLoader.loadSprite(spritePath);
    }
}