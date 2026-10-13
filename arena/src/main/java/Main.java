import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Hero first = createHero(scanner, 1);
        Hero second = createHero(scanner, 2);

        Arena arena = new Arena(first, second);
        arena.startTournament();
    }

    private static Hero createHero(Scanner scanner, int number) {
        System.out.println("Герой " + number);
        int type = readNumber(scanner, "Класс (1 - Воин, 2 - Маг): ");
        while (type != 1 && type != 2) {
            System.out.println("Введите 1 или 2");
            type = readNumber(scanner, "Класс (1 - Воин, 2 - Маг): ");
        }

        System.out.print("Имя: ");
        String name = scanner.nextLine();
        int maxHealth = readNumber(scanner, "Здоровье: ");
        int baseAttack = readNumber(scanner, "Атака: ");

        if (type == 1) {
            int armor = readNumber(scanner, "Броня: ");
            return new Warrior(name, maxHealth, baseAttack, armor);
        }
        int maxMana = readNumber(scanner, "Мана: ");
        return new Mage(name, maxHealth, baseAttack, maxMana);
    }

    private static int readNumber(Scanner scanner, String question) {
        while (true) {
            System.out.print(question);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число");
            }
        }
    }
}
