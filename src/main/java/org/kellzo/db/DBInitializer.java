package org.kellzo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBInitializer {
    private final Connection connection;

    public DBInitializer(Connection connection) {
        this.connection = connection;
    }

    public void createUsersTableIfNotExist() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "social_security_number VARCHAR(255) UNIQUE NOT NULL, " +
                "mobile_number VARCHAR(15) UNIQUE NOT NULL, " +
                "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    public void createAccountsTableIfNotExist() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "account_name VARCHAR(255) UNIQUE NOT NULL, " +
                "user_id INT NOT NULL, " +
                "balance DECIMAL(10, 2) DEFAULT 0.0, " +
                "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id))";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    public void createTransactionsTableIfNotExist() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "from_account_id INT NOT NULL, " +
                "to_account_id INT NOT NULL, " +
                "amount DECIMAL(10, 2) NOT NULL, " +
                "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (from_account_id) REFERENCES accounts(id), " +
                "FOREIGN KEY (to_account_id) REFERENCES accounts(id))";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}
