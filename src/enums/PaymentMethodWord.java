package enums;

public enum PaymentMethodWord {
    COINS("Монеты"),
    CARD("Банковская карта");

    private final String value;

    PaymentMethodWord(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
