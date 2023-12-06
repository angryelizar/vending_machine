package model;

public class CoinAcceptor extends PaymentMethod {
    private int amount;

    public CoinAcceptor(int amount) {
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