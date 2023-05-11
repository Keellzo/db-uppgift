package org.kellzo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserService {

    public static void addRowToUserTable(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Skriv in användarnamn, mail, mobilnummer och adress");
        String query = "INSERT INTO users (name, email, phone, adress) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, scanner.nextLine());
        preparedStatement.setString(2, scanner.nextLine());
        preparedStatement.setString(3, scanner.nextLine());
        preparedStatement.setString(4, scanner.nextLine());
        preparedStatement.executeUpdate();
    }

    public static List<User> getUserTable(Connection connection) throws SQLException {
        String query = "SELECT * FROM users";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setName(resultSet.getString("name"));
            user.setEmail(resultSet.getString("email"));
            user.setPhone(resultSet.getString("phone"));
            user.setAdress(resultSet.getString("adress"));
            user.setCreated(resultSet.getString("created"));
            user.setOnline(resultSet.getBoolean("online"));
            users.add(user);
        }
        return users;
    }

    public static void printUserTable(Connection connection) throws SQLException {
        List<User> users = getUserTable(connection);
        for (User user : users) {
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone: " + user.getPhone());
            System.out.println("Adress: " + user.getAdress());
            System.out.println("Created: " + user.getCreated());
            System.out.println("Online: " + user.isOnline());
            System.out.println("--------------------");
        }
    }

    public static List<User> getOnlineUsers(Connection connection) throws SQLException {
        String query = "SELECT * FROM users WHERE online = 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<User> onlineUsers = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setName(resultSet.getString("name"));
            user.setOnline(resultSet.getBoolean("online"));
            onlineUsers.add(user);
        }
        return onlineUsers;
    }

    public static void printOnlineUsers(Connection connection) throws SQLException {
        List<User> onlineUsers = getOnlineUsers(connection);
        for (User user : onlineUsers) {
            System.out.println("Name: " + user.getName());
            System.out.println("Online: " + user.isOnline());
            System.out.println("--------------------");
        }
    }
}
