package org.kellzo.models;

// The Account class represents a bank account
public class Account extends Model {
    private int userId;
    private double balance;
    private String accountName;

    public Account(int id, String created, int userId, double balance, String accountName) {
        super(id, created);
        this.userId = userId;
        this.balance = balance;
        this.accountName = accountName;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
