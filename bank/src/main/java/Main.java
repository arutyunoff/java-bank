import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Account acc = new Account(12345, 999, new BigDecimal("10000.00"), BankType.AUM);

        System.out.println("Добро пожаловать в банкомат!");
        System.out.print("Введите номер карты: ");
        if (!in.hasNextInt()) {
            System.out.println("Ошибка доступа.");
            return;
        }
        int num = in.nextInt();

        System.out.print("Введите пин-код: ");
        if (!in.hasNextInt()) {
            System.out.println("Ошибка доступа.");
            return;
        }
        int pin = in.nextInt();

        if (num != acc.getNum() || pin != acc.getPin()) {
            System.out.println("Ошибка доступа.");
            return;
        }

        CashMachine atm = new CashMachine();
        System.out.println(acc);

        System.out.print("Введите сумму внесения: ");
        BigDecimal sum = read(in);
        acc.bal = atm.plus(acc.getBal(), sum);
        System.out.println("Баланс: " + acc.getBal() + " руб.");

        System.out.print("Введите сумму снятия: ");
        sum = read(in);
        acc.bal = atm.minus(acc.getBal(), sum, acc.getBank());
        System.out.println("Баланс: " + acc.getBal() + " руб.");
    }

    public static BigDecimal read(Scanner in) {
        if (in == null || !in.hasNext()) {
            return null;
        }
        String s = in.next().replace(',', '.');
        if (!s.matches("[+-]?[0-9]+(\\.[0-9]+)?")) {
            return null;
        }
        return new BigDecimal(s);
    }
}
