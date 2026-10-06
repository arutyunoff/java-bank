import java.math.BigDecimal;

public enum BankType {
    NEO("НеоКредит Банк", "0.01"),
    AUM("Арум Финтех", "0.02"),
    VTA("Вектор Альянс Банк", "0.00");

    public final String name;
    public final BigDecimal fee;

    BankType(String name, String fee) {
        this.name = name;
        this.fee = new BigDecimal(fee);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getFee() {
        return fee;
    }
}
