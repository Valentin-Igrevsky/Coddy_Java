interface Damageable {
    int getHealth();
    void setHealth(int health);
    int getMaxHealth();
    void setMaxHealth(int maxHealth);
    void damage(int damage);
    void heal(int heal);
}