package org.kellzo.models;

public class Transaction extends Model {
    private int fromAccountId;
    private int toAccountId;
    private double amount;

    public Transaction(int id, String created, int fromAccountId, int toAccountId, double amount) {
        super(id, created);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }


    public Transaction() {
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
