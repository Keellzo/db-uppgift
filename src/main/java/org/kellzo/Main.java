package org.kellzo;

import org.kellzo.db.DBConnection;
import org.kellzo.db.DBInitializer;
import org.kellzo.view.Menu;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        try {
            Connection connection = DBConnection.getConnection();
            if (connection != null) {
                DBInitializer dbInitializer = new DBInitializer(connection);
                dbInitializer.createUsersTableIfNotExist();
                dbInitializer.createAccountsTableIfNotExist();
                dbInitializer.createTransactionsTableIfNotExist();

                Menu menu = new Menu(connection);
                menu.start();
            }

        } catch (SQLException e) {
            System.err.println("Anslutning till databasen misslyckades:");
            e.printStackTrace();
        }
    }
}