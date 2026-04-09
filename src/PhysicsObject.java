class PhysicsObject extends GameObject {
    protected double velX;
    protected double velY;

    protected boolean onGround;

    protected final double gravity = 0.5;

    public PhysicsObject(double x, double y) {
        super(x, y);
    }

    public void update() {
        onGround = false;

        x += velX;

        velY += gravity;
        y += velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public double getVelY() {
        return velY;
    }

    public double getVelX() {
        return velX;
    }

    public boolean getOnGround() {
        return onGround;
    }
}