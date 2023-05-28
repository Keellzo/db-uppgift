package org.kellzo.services;

import org.kellzo.models.Account;
import org.kellzo.models.Transaction;
import org.kellzo.models.User;
import org.kellzo.models.UserSummary;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
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

    public User loginUser(String socialSecurityNumber, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE social_security_number = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, socialSecurityNumber);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedHashedPassword = rs.getString("password");

            if (BCrypt.checkpw(password, storedHashedPassword)) {
                return new User(rs.getInt("id"), rs.getString("created"), rs.getString("username"), password, rs.getString("social_security_number"));
            }
        }

        throw new SQLException("Invalid login details");
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, social_security_number) VALUES (?, ?, ?)";


        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, user.getUsername());

        stmt.setString(2, hashedPassword);
        stmt.setString(3, user.getSocialSecurityNumber());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("created"), rs.getString("username"), rs.getString("password"), rs.getString("social_security_number"));
            } else {
                throw new SQLException("User not found with username: " + username);
            }
        }
    }

    public void removeUser(int id) throws SQLException {
        String sqlAccounts = "SELECT id FROM accounts WHERE user_id = ?";
        PreparedStatement stmtAccounts = connection.prepareStatement(sqlAccounts);
        stmtAccounts.setInt(1, id);
        ResultSet rs = stmtAccounts.executeQuery();

        while (rs.next()) {
            int accountId = rs.getInt("id");


            String sqlTransactions = "DELETE FROM transactions WHERE from_account_id = ? OR to_account_id = ?";
            PreparedStatement stmtTransactions = connection.prepareStatement(sqlTransactions);
            stmtTransactions.setInt(1, accountId);
            stmtTransactions.setInt(2, accountId);
            stmtTransactions.executeUpdate();
        }


        String sqlRemoveAccounts = "DELETE FROM accounts WHERE user_id = ?";
        PreparedStatement stmtRemoveAccounts = connection.prepareStatement(sqlRemoveAccounts);
        stmtRemoveAccounts.setInt(1, id);
        stmtRemoveAccounts.executeUpdate();


        String sqlRemoveUser = "DELETE FROM users WHERE id = ?";
        PreparedStatement stmtRemoveUser = connection.prepareStatement(sqlRemoveUser);
        stmtRemoveUser.setInt(1, id);
        stmtRemoveUser.executeUpdate();
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
        List<Account> accounts = accountService.getAccountsForUser(user);
        List<Transaction> transactions = new ArrayList<>();

        for (Account account : accounts) {
            transactions.addAll(transactionService.getTransactionsForAccount(account.getId()));
        }

        return new UserSummary(user, accounts, transactions);
    }
}