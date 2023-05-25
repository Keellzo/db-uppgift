package org.kellzo.services;

import org.kellzo.models.Account;
import org.kellzo.models.Transaction;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private final Connection connection;
    private final AccountService accountService;


    public TransactionService(Connection connection, AccountService accountService) {
        this.connection = connection;
        this.accountService = accountService;
    }

    private void createTableIfNotExist() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "from_account_id INT NOT NULL, " +
                "to_account_id INT NOT NULL, " +
                "amount DECIMAL(10, 2) NOT NULL, " +
                "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (from_account_id) REFERENCES accounts(id), " +
                "FOREIGN KEY (to_account_id) REFERENCES accounts(id))";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }


    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (from_account_id, to_account_id, amount, created) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, transaction.getFromAccountId());
        stmt.setInt(2, transaction.getToAccountId());
        stmt.setDouble(3, transaction.getAmount());
        stmt.setString(4, transaction.getCreated());
        stmt.executeUpdate();
    }

    public void sendTransaction(int fromAccountId, int toAccountId, double amount) throws SQLException {
        Account fromAccount = accountService.getAccount(fromAccountId);

        if (fromAccount.getBalance() < amount) {
            throw new SQLException("Insufficient funds");
        }

        String updateSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateSql);
        updateStmt.setDouble(1, amount);
        updateStmt.setInt(2, fromAccountId);
        updateStmt.executeUpdate();

        updateSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        updateStmt = connection.prepareStatement(updateSql);
        updateStmt.setDouble(1, amount);
        updateStmt.setInt(2, toAccountId);
        updateStmt.executeUpdate();

        addTransaction(new Transaction(null, fromAccountId, toAccountId, amount));
    }


    public List<Transaction> getTransactions(int accountId, Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE (from_account_id = ? OR to_account_id = ?) AND created BETWEEN ? AND ? ORDER BY created ASC";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, accountId);
        stmt.setInt(2, accountId);
        stmt.setDate(3, startDate);
        stmt.setDate(4, endDate);
        ResultSet rs = stmt.executeQuery();

        List<Transaction> transactions = new ArrayList<>();
        while (rs.next()) {
            Transaction transaction = new Transaction(rs.getInt("id"), rs.getString("created"), rs.getInt("from_account_id"), rs.getInt("to_account_id"), rs.getDouble("amount"));
            transactions.add(transaction);
        }


        return transactions;
    }

    public List<Transaction> getTransactionsForUser(int userId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, userId);
        stmt.setInt(2, userId);
        ResultSet rs = stmt.executeQuery();

        List<Transaction> transactions = new ArrayList<>();
        while (rs.next()) {
            transactions.add(new Transaction(rs.getInt("id"), rs.getString("created"), rs.getInt("fromAccountId"), rs.getInt("toAccountId"), rs.getDouble("amount")));
        }

        return transactions;
    }


}

