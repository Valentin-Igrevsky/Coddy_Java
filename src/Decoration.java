class Decoration extends GameObject {
    public Decoration(double x, double y, String spritePath) {
        super(x, y);
        this.SPRITE = SpriteLoader.loadSprite(spritePath);
    }
}