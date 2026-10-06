import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {
    public int num;
    public int pin;
    public BigDecimal bal;
    public BankType bank;

    public Account(int num, int pin, BigDecimal bal, BankType bank) {
        if (num >= 10000 && num <= 99999) {
            this.num = num;
        } else {
            this.num = 10000;
        }

        if (pin >= 100 && pin <= 999) {
            this.pin = pin;
        } else {
            this.pin = 100;
        }

        if (bal == null || bal.signum() < 0) {
            this.bal = BigDecimal.ZERO.setScale(2);
        } else {
            this.bal = bal.setScale(2, RoundingMode.HALF_UP);
        }

        if (bank == null) {
            this.bank = BankType.NEO;
        } else {
            this.bank = bank;
        }
    }

    public int getNum() {
        return num;
    }

    public int getPin() {
        return pin;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public BankType getBank() {
        return bank;
    }

    @Override
    public String toString() {
        return bank.getName() + " Карта: " + num + ", Баланс: " + bal + " руб.";
    }
}
