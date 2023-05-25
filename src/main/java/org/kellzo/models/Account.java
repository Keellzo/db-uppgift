package org.kellzo.models;

public class Account extends Model {
    private int userId;
    private double balance;

    private String accountNumber;

    public Account(int id, String created, int userId, double balance, String accountNumber) {
        super(id, created);
        this.userId = userId;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public Account() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
