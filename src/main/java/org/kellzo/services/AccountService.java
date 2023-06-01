package org.kellzo.services;

import org.kellzo.models.Account;
import org.kellzo.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// The AccountService class encapsulates the business logic related to account operations
public class AccountService {
    private final Connection connection;

    public AccountService(Connection connection) {
        this.connection = connection;
    }

    // Method to get an account by its ID
    public Account getAccountById(int id) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Account(rs.getInt("id"), rs.getString("created"), rs.getInt("user_id"), rs.getDouble("balance"), rs.getString("account_name"));
        } else {
            throw new SQLException("Account not found");
        }
    }

    // Method to get an account by its name
    public Account getAccountByAccountName(String accountName) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_name = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, accountName);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Account(rs.getInt("id"), rs.getString("created"), rs.getInt("user_id"), rs.getDouble("balance"), rs.getString("account_name"));
        } else {
            throw new SQLException("Account not found");
        }
    }

    // Method to get the first account of a user by user ID
    public Account getFirstAccountByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM Accounts WHERE user_id = ? ORDER BY id ASC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return createAccountFromResultSet(resultSet);
            } else {
                throw new SQLException("User has no accounts.");
            }
        }
    }

    // Helper method to create an Account object from a ResultSet
    private Account createAccountFromResultSet(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("id"),
                rs.getString("created"),
                rs.getInt("user_id"),
                rs.getDouble("balance"),
                rs.getString("account_name")
        );
    }

    // Method to update an account in the database
    public void updateAccount(Account account) throws SQLException {
        String query = "UPDATE Accounts SET balance = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setDouble(1, account.getBalance());
        statement.setInt(2, account.getId());

        int updated = statement.executeUpdate();

        if (updated != 1) {
            throw new SQLException("Could not update account.");
        }
    }

    // Method to add an account to the database
    public void addAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (user_id, balance, account_name) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, account.getUserId());
        stmt.setDouble(2, account.getBalance());
        stmt.setString(3, account.getAccountName());
        stmt.executeUpdate();
    }

    // Method to remove an account from the database
    public void removeAccount(String accountName) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_name = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, accountName);
        stmt.executeUpdate();
    }

    // Method to get all accounts for a user
    public List<Account> getAccountsForUser(User user) throws SQLException {
        String sql = "SELECT * FROM users JOIN accounts ON users.id = accounts.user_id WHERE users.id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, user.getId());
        ResultSet rs = stmt.executeQuery();

        List<Account> accounts = new ArrayList<>();
        while (rs.next()) {
            Account account = new Account(
                    rs.getInt("accounts.id"),
                    rs.getString("accounts.created"),
                    rs.getInt("accounts.user_id"),
                    rs.getDouble("accounts.balance"),
                    rs.getString("accounts.account_name")
            );
            accounts.add(account);
        }

        return accounts;
    }
}