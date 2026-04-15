class DamageZone extends GameObject {
    private final int damage;

    private int cooldown = 0;
    private final int maxCooldown = 30;

    public DamageZone(double x, double y, String spritePath, int damage) {
        super(x, y);
        this.SPRITE = SpriteLoader.loadSprite(spritePath);
        this.damage = damage;
    }

    public void update() {
        if (cooldown > 0) cooldown--;
    }

    public void checkCollision(GameObject obj) {
        if (cooldown > 0) return;
        if (this.getBounds().intersects(obj.getBounds())) {

            if (obj instanceof Damageable target) {
                target.damage(damage);
                cooldown = maxCooldown;
            }
        }
    }
}