package org.kellzo.view;

import org.kellzo.models.*;
import org.kellzo.services.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private User currentUser = null;

    public Menu(Connection connection) {
        this.accountService = new AccountService(connection);
        this.transactionService = new TransactionService(connection, accountService);
        this.userService = new UserService(connection, accountService, transactionService);

        this.transactionService.setUserService(userService);
    }


    public void start() throws SQLException {
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);

        while (!exit) {
            if (currentUser == null) {
                System.out.println("Select an option: ");
                System.out.println("1. Add User");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                int option;
                try {
                    option = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                    continue;
                }


                switch (option) {
                    case 1 -> {
                        System.out.println("Enter username:");
                        String addUsername = scanner.nextLine();
                        System.out.println("Enter password:");
                        String addPassword = scanner.nextLine();
                        System.out.println("Enter social security number:");
                        String addSocialSecurityNumber = scanner.nextLine();
                        System.out.println("Enter mobile number:");
                        String addMobileNumber = scanner.nextLine();
                        User newUser = new User();
                        newUser.setUsername(addUsername);
                        newUser.setPassword(addPassword);
                        newUser.setSocialSecurityNumber(addSocialSecurityNumber);
                        newUser.setMobileNumber(addMobileNumber);
                        userService.addUser(newUser);
                        currentUser = newUser;
                        System.out.println("User successfully added!");
                    }
                    case 2 -> {
                        System.out.println("Enter social security number:");
                        String loginSocialSecurityNumber = scanner.nextLine();
                        System.out.println("Enter password:");
                        String loginPassword = scanner.nextLine();

                        try {
                            currentUser = userService.loginUser(loginSocialSecurityNumber, loginPassword);
                            System.out.println("Login successful!");
                        } catch (SQLException e) {
                            System.out.println("Login failed: " + e.getMessage());
                        }
                    }
                    case 3 -> exit = true;
                    default -> System.out.println("Invalid option. Please choose a valid one.");
                }
            } else {
                System.out.println("Welcome, " + currentUser.getUsername() + "!");
                System.out.println("Select an option: ");
                System.out.println("1. Add Account");
                System.out.println("2. Remove Account");
                System.out.println("3. Update User Details");
                System.out.println("4. List Transactions from a certain date");
                System.out.println("5. Display User Summary");
                System.out.println("6. Logout");
                System.out.println("7. Remove My User Account");
                System.out.println("8. Send Transaction to Another User");
                System.out.println("9. Exit");


                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> {
                        System.out.println("Enter account name:");
                        String accountName = scanner.nextLine();
                        System.out.println("Enter initial balance:");
                        double initialBalance = scanner.nextDouble();
                        scanner.nextLine();
                        Account newAccount = new Account(0, null, currentUser.getId(), initialBalance, accountName);
                        accountService.addAccount(newAccount);
                        System.out.println("Account successfully added!");
                    }
                    case 2 -> {
                        System.out.println("Enter account name to remove:");
                        String accountNameToRemove = scanner.nextLine();
                        accountService.removeAccount(accountNameToRemove);
                        System.out.println("Account successfully removed!");
                    }
                    case 3 -> {
                        System.out.println("Enter new username:");
                        String newUsername = scanner.nextLine();
                        System.out.println("Enter new password:");
                        String newPassword = scanner.nextLine();
                        System.out.println("Enter new social security number:");
                        String newSocialSecurityNumber = scanner.nextLine();
                        System.out.println("Enter new mobile number:");
                        String newMobileNumber = scanner.nextLine();
                        currentUser.setUsername(newUsername);
                        currentUser.setPassword(newPassword);
                        currentUser.setSocialSecurityNumber(newSocialSecurityNumber);
                        currentUser.setMobileNumber(newMobileNumber);
                        userService.updateUser(currentUser);
                        System.out.println("User details successfully updated!");
                    }
                    case 4 -> {
                        System.out.println("Enter account name:");
                        String accountNameForTransactions = scanner.nextLine();
                        Account account = accountService.getAccountByAccountName(accountNameForTransactions);
                        System.out.println("Enter start date (format YYYY-MM-DD):");
                        String startDateInput = scanner.nextLine();
                        System.out.println("Enter end date (format YYYY-MM-DD):");
                        String endDateInput = scanner.nextLine();

                        LocalDate startDateLocalDate = LocalDate.parse(startDateInput);
                        LocalDate endDateLocalDate = LocalDate.parse(endDateInput).plusDays(1);
                        LocalDateTime startDateTime = startDateLocalDate.atStartOfDay();
                        LocalDateTime endDateTime = endDateLocalDate.atStartOfDay();

                        List<Transaction> transactions = transactionService.getTransactionsForAccountBetweenDates(account.getId(), startDateTime, endDateTime);
                        System.out.println("Transactions for account " + accountNameForTransactions + ":");
                        for (Transaction transaction : transactions) {
                            String fromAccountName = accountService.getAccountById(transaction.getFromAccountId()).getAccountName();
                            String toAccountName = accountService.getAccountById(transaction.getToAccountId()).getAccountName();
                            System.out.println(
                                    "Transaction ID: " + transaction.getId() +
                                            ", Created at: " + transaction.getCreated() +
                                            ", From Account Name: " + fromAccountName +
                                            ", To Account Name: " + toAccountName +
                                            ", Amount: " + transaction.getAmount()
                            );
                        }
                    }
                    case 5 -> {
                        UserSummary userSummary = userService.getUserSummary(currentUser.getId());
                        User user = userSummary.getUser();

                        System.out.println("User: " + user.getUsername());
                        System.out.println("Social Security Number: " + user.getSocialSecurityNumber());
                        System.out.println("Mobile Number: " + user.getMobileNumber());

                        System.out.println("\nAccounts:");
                        for (Account accounts : userSummary.getAccounts()) {
                            System.out.println("Account ID: " + accounts.getId() +
                                    ", Balance: " + accounts.getBalance() +
                                    ", Account Name: " + accounts.getAccountName());
                        }
                        System.out.println("\nTransactions:");
                        for (Transaction transaction : userSummary.getTransactions()) {
                            String fromAccountName = accountService.getAccountById(transaction.getFromAccountId()).getAccountName();
                            String toAccountName = accountService.getAccountById(transaction.getToAccountId()).getAccountName();

                            System.out.println("Transaction ID: " + transaction.getId() +
                                    ", Created at: " + transaction.getCreated() +
                                    ", From Account Name: " + fromAccountName +
                                    ", To Account Name: " + toAccountName +
                                    ", Amount: " + transaction.getAmount());
                        }
                    }
                    case 6 -> {
                        currentUser = null;
                        System.out.println("You have logged out.");
                    }
                    case 7 -> {
                        userService.removeUser(currentUser.getId());
                        currentUser = null;
                        System.out.println("Your user account has been successfully removed.");
                    }
                    case 8 -> {
                        System.out.println("Enter source account name:");
                        String sourceAccountName = scanner.nextLine();
                        System.out.println("Enter destination mobile number:");
                        String destinationMobileNumber = scanner.nextLine();
                        System.out.println("Enter amount:");
                        double amountToUser = scanner.nextDouble();
                        scanner.nextLine();
                        try {
                            transactionService.sendTransactionToUser(sourceAccountName, destinationMobileNumber, amountToUser);
                            System.out.println("Transaction successfully sent!");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        } catch (SQLException e) {
                            System.out.println("Transaction failed: " + e.getMessage());
                        }
                    }
                    case 9 -> exit = true;
                    default -> System.out.println("Invalid option. Please choose a valid one.");
                }
            }
        }
    }


}
