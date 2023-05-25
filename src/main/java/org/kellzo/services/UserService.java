package org.kellzo.services;

import org.kellzo.models.Account;
import org.kellzo.models.Transaction;
import org.kellzo.models.User;
import org.kellzo.models.UserSummary;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.List;

public class UserService {
    private final Connection connection;
    private final AccountService accountService;
    private final TransactionService transactionService;


    public UserService(Connection connection, AccountService accountService, TransactionService transactionService) {
        this.connection = connection;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    private void createTableIfNotExist() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "social_security_number VARCHAR(255) UNIQUE NOT NULL, " +
                "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }

    public User loginUser(String socialSecurityNumber, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE social_security_number = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, socialSecurityNumber);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedHashedPassword = rs.getString("password");

            if (BCrypt.checkpw(password, storedHashedPassword)) {
                return new User(rs.getInt("id"), rs.getString("created"), rs.getString("username"), rs.getString("password"), rs.getString("social_security_number"));
            }
        }

        throw new SQLException("Invalid login details");
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, social_security_number, created) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        stmt.setString(1, user.getUsername());
        stmt.setString(2, hashedPassword);
        stmt.setString(3, user.getSocialSecurityNumber());
        stmt.setString(4, user.getCreated());
        stmt.executeUpdate();
    }

    public void removeUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, social_security_number = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        stmt.setString(1, user.getUsername());
        stmt.setString(2, hashedPassword);
        stmt.setString(3, user.getSocialSecurityNumber());
        stmt.setInt(4, user.getId());
        stmt.executeUpdate();
    }

    public User getUser(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new User(rs.getInt("id"), rs.getString("created"), rs.getString("username"), rs.getString("password"), rs.getString("social_security_number"));
        } else {
            return null;
        }
    }

    public UserSummary getUserSummary(int userId) throws SQLException {
        User user = getUser(userId);
        List<Account> accounts = accountService.getAccountsForUser(userId);
        List<Transaction> transactions = transactionService.getTransactionsForUser(userId);

        return new UserSummary(user, accounts, transactions);
    }

}
