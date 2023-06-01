package org.kellzo.services;

import org.kellzo.models.Account;
import org.kellzo.models.Transaction;
import org.kellzo.models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// The TransactionService class encapsulates the business logic related to transaction operations
public class TransactionService {
    private final Connection connection;
    private final AccountService accountService;
    private UserService userService;

    // Constructor to initialize the TransactionService with required dependencies
    public TransactionService(Connection connection, AccountService accountService) {
        this.connection = connection;
        this.accountService = accountService;
    }

    // Setter for UserService dependency
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // Method to add a new transaction
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (from_account_id, to_account_id, amount) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, transaction.getFromAccountId());
        stmt.setInt(2, transaction.getToAccountId());
        stmt.setDouble(3, transaction.getAmount());
        stmt.executeUpdate();
    }

    // Method to transfer money from one user to another
    public void sendTransactionToUser(String sourceAccountName, String destinationMobileNumber, double amount) throws SQLException {
        Account sourceAccount = accountService.getAccountByAccountName(sourceAccountName);

        User destinationUser = userService.getUserByMobileNumber(destinationMobileNumber);
        if (destinationUser == null) {
            throw new IllegalArgumentException("Destination user does not exist.");
        }

        Account destinationAccount = accountService.getFirstAccountByUserId(destinationUser.getId());

        if (sourceAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Source account does not have enough balance for the transaction.");
        }

        Transaction transaction = new Transaction();
        transaction.setFromAccountId(sourceAccount.getId());
        transaction.setToAccountId(destinationAccount.getId());
        transaction.setAmount(amount);

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountService.updateAccount(sourceAccount);
        accountService.updateAccount(destinationAccount);
        addTransaction(transaction);
    }

    // Private helper method to extract a list of transactions from a ResultSet
    private List<Transaction> getTransactions(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();

        List<Transaction> transactions = new ArrayList<>();
        while (rs.next()) {
            transactions.add(new Transaction(rs.getInt("id"), rs.getString("created"), rs.getInt("from_account_id"), rs.getInt("to_account_id"), rs.getDouble("amount")));
        }

        return transactions;
    }

    // Method to get all transactions for an account within a specific date range
    public List<Transaction> getTransactionsForAccountBetweenDates(int accountId, LocalDateTime startDateTime, LocalDateTime endDateTime) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE (from_account_id = ? OR to_account_id = ?) AND (created BETWEEN ? AND ?) ORDER BY created";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, accountId);
        stmt.setInt(2, accountId);
        stmt.setTimestamp(3, Timestamp.valueOf(startDateTime));
        stmt.setTimestamp(4, Timestamp.valueOf(endDateTime));
        return getTransactions(stmt);
    }


    // Method to get all transactions for an account
    public List<Transaction> getTransactionsForAccount(int accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, accountId);
        stmt.setInt(2, accountId);
        return getTransactions(stmt);
    }
}