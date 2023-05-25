package org.kellzo.models;

import java.util.List;

public class UserSummary {
    private User user;
    private List<Account> accounts;
    private List<Transaction> transactions;

    public UserSummary(User user, List<Account> accounts, List<Transaction> transactions) {
        this.user = user;
        this.accounts = accounts;
        this.transactions = transactions;
    }

    public User getUser() {
        return user;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}

