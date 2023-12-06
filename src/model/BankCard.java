package model;

public class BankCard extends PaymentMethod {
    private int amount;

    public BankCard(int amount) {
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
