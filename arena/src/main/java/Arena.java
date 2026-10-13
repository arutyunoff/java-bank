public class Arena {
    private final Hero fighter1;
    private final Hero fighter2;
    private int roundCounter;

    public Arena(Hero fighter1, Hero fighter2) {
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
    }

    public void startTournament() {
        if (fighter1 == null || fighter2 == null) {
            System.out.println("Для поединка нужны два героя");
            return;
        }

        while (fighter1.isAlive() && fighter2.isAlive()) {
            roundCounter++;
            System.out.println();
            System.out.println("Раунд " + roundCounter);

            Hero attacker;
            Hero defender;
            if (Math.random() < 0.5) {
                attacker = fighter1;
                defender = fighter2;
            } else {
                attacker = fighter2;
                defender = fighter1;
            }

            ActionType action = attacker.makeTurn(defender);
            System.out.println("Игрок " + attacker.getName() + " совершил действие: " + action.getTitle());

            if (!defender.isAlive()) {
                break;
            }

            action = defender.makeTurn(attacker);
            System.out.println("Игрок " + defender.getName() + " совершил действие: " + action.getTitle());

            System.out.println(fighter1);
            System.out.println(fighter2);
        }

        Hero winner;
        if (fighter1.isAlive()) {
            winner = fighter1;
        } else {
            winner = fighter2;
        }
        System.out.println();
        System.out.println("Победитель: " + winner.getName());
    }
}
