public class Mage extends Hero implements Castable, Restable {
    private static final int SHOT_COST = 10;
    private static final int STAFF_MANA = 5;
    private static final int FIREBALL_COST = 25;

    private final int maxMana;
    private int mana;

    public Mage(String name, int maxHealth, int baseAttack, int maxMana) {
        super(name, maxHealth, baseAttack);
        this.maxMana = Math.max(maxMana, MIN_STAT_VALUE);
        this.mana = this.maxMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMana() {
        return mana;
    }

    @Override
    public void attack(Hero target) {
        if (target == null) {
            System.out.println(getName() + " не видит противника");
            return;
        }
        if (mana >= SHOT_COST) {
            int damage = getBaseAttack() * 2;
            mana -= SHOT_COST;
            System.out.println(getName() + " выпускает Магический выстрел в противника " + target.getName()
                    + ", урон " + damage + ", мана " + mana + "/" + maxMana);
            target.takeDamage(damage);
        } else {
            int damage = getBaseAttack() / 2;
            mana = Math.min(mana + STAFF_MANA, maxMana);
            System.out.println(getName() + " бьет посохом противника " + target.getName()
                    + ", урон " + damage + ", мана " + mana + "/" + maxMana);
            target.takeDamage(damage);
        }
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
        return mana >= FIREBALL_COST;
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null) {
            System.out.println(getName() + " не видит противника");
            return;
        }
        if (!canCast()) {
            System.out.println(getName() + " не смог создать Огненную глыбу: не хватает маны");
            return;
        }
        int damage = getBaseAttack() * 3;
        mana -= FIREBALL_COST;
        System.out.println(getName() + " бросает Огненную глыбу в противника " + target.getName()
                + ", урон " + damage + ", мана " + mana + "/" + maxMana);
        target.takeDamage(damage);
    }

    @Override
    public boolean needsRest() {
        return mana == 0 || getHealth() < getMaxHealth() * 0.3;
    }

    @Override
    public void rest() {
        mana = maxMana;
        heal(getBaseAttack());
        System.out.println(getName() + " медитирует, мана " + mana + "/" + maxMana
                + ", здоровье " + getHealth() + "/" + getMaxHealth());
    }

    @Override
    public String toString() {
        return "Маг " + super.toString() + ", мана " + mana + "/" + maxMana;
    }
}
