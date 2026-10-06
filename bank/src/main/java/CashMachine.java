import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashMachine implements WithdrawalOperations, DepositOperations {
    @Override
    public BigDecimal plus(BigDecimal bal, BigDecimal sum) {
        if (bal == null) {
            bal = BigDecimal.ZERO.setScale(2);
        }
        if (sum == null || sum.signum() <= 0) {
            return bal;
        }
        return bal.add(sum).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal minus(BigDecimal bal, BigDecimal sum, BankType bank) {
        if (bal == null) {
            bal = BigDecimal.ZERO.setScale(2);
        }
        if (sum == null || sum.signum() <= 0) {
            return bal;
        }
        BigDecimal fee = applyCommission(sum, bank);
        BigDecimal total = sum.add(fee);
        if (total.compareTo(bal) > 0) {
            System.out.println("Недостаточно средств.");
            return bal;
        }
        return bal.subtract(total).setScale(2, RoundingMode.HALF_UP);
    }
}
