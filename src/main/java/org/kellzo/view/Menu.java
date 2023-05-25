package org.kellzo.view;

import org.kellzo.models.*;
import org.kellzo.services.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private User currentUser = null;

    public Menu(Connection connection) {
        this.userService = new UserService(connection, new AccountService(connection), new TransactionService(connection, new AccountService(connection)));
        this.accountService = new AccountService(connection);
        this.transactionService = new TransactionService(connection, new AccountService(connection));
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

                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.println("Enter account number:");
                        String accountNumber = scanner.nextLine();
                        System.out.println("Enter initial balance:");
                        double initialBalance = scanner.nextDouble();
                        scanner.nextLine(); // consume the newline
                        Account newAccount = new Account(0, null, currentUser.getId(), initialBalance, accountNumber);
                        accountService.addAccount(newAccount);
                        System.out.println("Account successfully added!");
                        break;

                    case 2:
                        System.out.println("Enter your username:");
                        String loginUsername = scanner.nextLine();
                        System.out.println("Enter your password:");
                        String loginPassword = scanner.nextLine();
                        User user = userService.loginUser(loginUsername, loginPassword);
                        if (user != null) {
                            currentUser = user;
                            System.out.println("Successfully logged in!");
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose a valid one.");
                }
            } else {
                System.out.println("Select an option: ");
                System.out.println("1. Add Account");
                System.out.println("2. Remove Account");
                System.out.println("3. Update User Details");
                System.out.println("4. Send Transaction");
                System.out.println("5. List Transactions");
                System.out.println("6. Display User Summary");
                System.out.println("7. Logout");
                System.out.println("8. Exit");

                int option = scanner.nextInt();
                scanner.nextLine(); // consume the newline

                switch (option) {
                    case 1:
                        System.out.println("Enter account number:");
                        String accountNumber = scanner.nextLine();
                        System.out.println("Enter initial balance:");
                        double initialBalance = scanner.nextDouble();
                        scanner.nextLine(); // consume the newline
                        Account newAccount = new Account(0, null, currentUser.getId(), initialBalance, accountNumber);
                        accountService.addAccount(newAccount);
                        System.out.println("Account successfully added!");
                        break;

                    case 2:
                        System.out.println("Enter account number to remove:");
                        String accountNumberToRemove = scanner.nextLine();
                        accountService.removeAccount(accountNumberToRemove);
                        System.out.println("Account successfully removed!");
                        break;

                    case 3:
                        // I'm assuming updateUser takes in a User object and updates the details in the database.
                        // You may need to write some additional code here to get the new user details from the user.
                        userService.updateUser(currentUser);
                        System.out.println("User details successfully updated!");
                        break;

                    case 4:
                        System.out.println("Enter source account number:");
                        String sourceAccount = scanner.nextLine();
                        System.out.println("Enter destination account number:");
                        String destinationAccount = scanner.nextLine();
                        System.out.println("Enter amount:");
                        double amount = scanner.nextDouble();
                        scanner.nextLine(); // consume the newline
                        transactionService.sendTransaction(sourceAccount, destinationAccount, amount);
                        System.out.println("Transaction successfully sent!");
                        break;

                    case 5:
                        System.out.println("Enter account number:");
                        String accountNumberForTransactions = scanner.nextLine();
                        List<Transaction> transactions = transactionService.getTransactionsForUser(currentUser.getId());
                        System.out.println("Transactions for account " + accountNumberForTransactions + ":");
                        for (Transaction transaction : transactions) {
                            System.out.println(transaction);
                        }
                        break;

                    case 6:
                        System.out.println(currentUser);
                        List<Account> accounts = accountService.getAccountsForUser(currentUser.getId());
                        for (Account account : accounts) {
                            System.out.println(account);
                        }
                        break;

                    case 7:
                        currentUser = null;
                        System.out.println("You have logged out.");
                        break;

                    case 8:
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid option. Please choose a valid one.");
                }
            }
        }
    }


}
