package enums;

public enum PaymentMethodWord {
    COINS("Монеты"),
    CARD("Банковская карта"),
    NONE("Не указан");

    private final String value;

    PaymentMethodWord(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
