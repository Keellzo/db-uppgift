package org.kellzo.services;

import org.kellzo.models.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private final Connection connection;

    public AccountService(Connection connection) {
        this.connection = connection;
    }

    private void createTableIfNotExist() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "account_number VARCHAR(255) UNIQUE NOT NULL, " +
                "user_id INT NOT NULL, " +
                "balance DECIMAL(10, 2) DEFAULT 0.0, " +
                "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id))";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }

    public Account getAccount(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, accountId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Account(rs.getInt("id"), rs.getString("created"), rs.getInt("user_id"), rs.getDouble("balance"), rs.getString("account_number"));
        } else {
            throw new SQLException("Account not found");
        }
    }


    public void addAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (user_id, balance, account_number, created) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, account.getUserId());
        stmt.setDouble(2, account.getBalance());
        stmt.setString(3, account.getAccountNumber());
        stmt.setString(4, account.getCreated());
        stmt.executeUpdate();
    }

    public void removeAccount(String accountNumber) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, accountNumber);
        stmt.executeUpdate();
    }


    public List<Account> getAccountsForUser(int userId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Account> accounts = new ArrayList<>();
                while (rs.next()) {
                    Account account = new Account(rs.getInt("id"), rs.getString("created"), rs.getInt("user_id"), rs.getDouble("balance"), rs.getString("account_number"));
                    accounts.add(account);
                }
                return accounts;
            }
        }
    }


}

