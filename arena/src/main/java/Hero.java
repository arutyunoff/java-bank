public abstract class Hero {
    public static final int MIN_STAT_VALUE = 1;

    private final String name;
    private final int maxHealth;
    private final int baseAttack;
    private int health;

    public Hero(String name, int maxHealth, int baseAttack) {
        if (name == null || name.isBlank()) {
            this.name = "Безымянный герой";
        } else {
            this.name = name;
        }
        this.maxHealth = Math.max(maxHealth, MIN_STAT_VALUE);
        this.baseAttack = Math.max(baseAttack, MIN_STAT_VALUE);
        this.health = this.maxHealth;
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getHealth() {
        return health;
    }

    public abstract void attack(Hero target);

    public abstract ActionType makeTurn(Hero target);

    public void takeDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        health = Math.max(health - damage, 0);
        if (health == 0) {
            System.out.println(name + " пал в бою!");
        }
    }

    public void heal(int amount) {
        if (amount <= 0) {
            return;
        }
        health = Math.min(health + amount, maxHealth);
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public String toString() {
        return name + ": здоровье " + health + "/" + maxHealth;
    }
}
