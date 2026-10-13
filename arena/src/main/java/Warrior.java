public class Warrior extends Hero implements Castable, Restable {
    private final int armor;

    public Warrior(String name, int maxHealth, int baseAttack, int armor) {
        super(name, maxHealth, baseAttack);
        this.armor = Math.max(armor, MIN_STAT_VALUE);
    }

    public int getArmor() {
        return armor;
    }

    @Override
    public void takeDamage(int damage) {
        int finalDamage = Math.max(damage - armor, 1);
        super.takeDamage(finalDamage);
    }

    @Override
    public void attack(Hero target) {
        if (target == null) {
            System.out.println(getName() + " не видит противника");
            return;
        }
        System.out.println(getName() + " бьет мечом противника " + target.getName() + ", урон " + getBaseAttack());
        target.takeDamage(getBaseAttack());
    }

    @Override
    public ActionType makeTurn(Hero target) {
        if (canCast()) {
            castSpecialSkill(target);
            return ActionType.SPECIAL_SKILL;
        }
        if (needsRest()) {
            rest();
            return ActionType.REST;
        }
        attack(target);
        return ActionType.BASE_ATTACK;
    }

    @Override
    public boolean canCast() {
        return getHealth() < getMaxHealth() * 0.5;
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null) {
            System.out.println(getName() + " не видит противника");
            return;
        }
        if (!canCast()) {
            System.out.println("Воин попытался ударить щитом, но потерял равновесие!");
            return;
        }
        int damage = getBaseAttack() + armor * 2;
        System.out.println(getName() + " бьет щитом противника " + target.getName() + ", урон " + damage);
        target.takeDamage(damage);
    }

    @Override
    public boolean needsRest() {
        return getHealth() < getMaxHealth() * 0.15;
    }

    @Override
    public void rest() {
        heal(armor * 2);
        System.out.println(getName() + " использует Второе дыхание, здоровье " + getHealth() + "/" + getMaxHealth());
    }

    @Override
    public String toString() {
        return "Воин " + super.toString();
    }
}
