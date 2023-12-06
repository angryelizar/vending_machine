package model;

public abstract class PaymentMethod {
    private int amount;

    public abstract int getAmount();
    public abstract void setAmount(int amount);
}
