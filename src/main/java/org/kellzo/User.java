package org.kellzo;

import java.sql.*;
import java.util.Scanner;

public class User {

    public static void addRowToUserTable(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("Skriv in användarnamn, mail, mobilnummer och adress");
        String query = "INSERT INTO users (name, email, phone, adress) VALUES ('" + scanner.nextLine() + "', '" + scanner.nextLine() + "', '" + scanner.nextLine() + "', '" + scanner.nextLine() + "')";
        System.out.println();
        statement.executeUpdate(query);
    }

    public static void printUserTable(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

        while (resultSet.next()) {
            System.out.println("Användarnamn: " + resultSet.getString("name"));
            System.out.println("Email: " + resultSet.getString("email"));
            System.out.println("Created: " + resultSet.getString("created"));
            System.out.println("Online: " + resultSet.getString("online"));
            System.out.println("Telefonnummer: " + resultSet.getString("phone"));
            System.out.println("Adress: " + resultSet.getString("adress"));

            System.out.println("--------------------");
        }
    }

    public static void getOnlineUsers(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE online = 1");

        while (resultSet.next()) {
            System.out.println("Email: " + resultSet.getString("email"));
            System.out.println("--------------------");
        }
    }
}