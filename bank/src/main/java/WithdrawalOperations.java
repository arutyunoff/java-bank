import java.math.BigDecimal;
import java.math.RoundingMode;

public interface WithdrawalOperations {
    BigDecimal minus(BigDecimal bal, BigDecimal sum, BankType bank);

    default BigDecimal applyCommission(BigDecimal sum, BankType bank) {
        if (sum == null || bank == null) {
            return BigDecimal.ZERO.setScale(2);
        }
        return sum.multiply(bank.getFee()).setScale(2, RoundingMode.HALF_UP);
    }
}
