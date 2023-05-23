package org.kellzo.services;

import java.io.IOException;
import java.util.logging.*;

import org.kellzo.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserService {


    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private static FileHandler fh;

    // static block to setup the FileHandler
    static {
        try {
            fh = new FileHandler("user-creation.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to set the logging level
    public static void toggleLogging(String state) {
        switch (state.toLowerCase()) {
            case "on":
                logger.setLevel(Level.ALL);
                break;
            case "off":
                logger.setLevel(Level.OFF);
                break;
            default:
                System.out.println("Invalid state. Please enter 'on' or 'off'.");
        }
    }


    public static void addRowToUserTable(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter username, email, phone, address and password");
        String query = "INSERT INTO users (name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        String name = scanner.nextLine();
        String email = scanner.nextLine();
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, scanner.nextLine());
        preparedStatement.setString(4, scanner.nextLine());

        String passwordPlainText = scanner.nextLine();
        String hashedPassword = BCrypt.hashpw(passwordPlainText, BCrypt.gensalt());
        preparedStatement.setString(5, hashedPassword);

        preparedStatement.executeUpdate();

        // Get the auto generated key (user_id)
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int userId = generatedKeys.getInt(1);

            // Fetch the created timestamp from database
            String fetchQuery = "SELECT created FROM users WHERE users_id = ?";
            PreparedStatement fetchStatement = connection.prepareStatement(fetchQuery);
            fetchStatement.setInt(1, userId);
            ResultSet fetchResult = fetchStatement.executeQuery();
            if (fetchResult.next()) {
                Timestamp createdTimestamp = fetchResult.getTimestamp("created");

                // Log the user creation
                logger.log(Level.INFO, createdTimestamp + " Användare ”" + name + "” skapades med epost ”" + email + "”.");
            }
        }
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
            user.setAdress(resultSet.getString("address"));
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

    public static void deleteUser(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter user ID:");
        int userId = scanner.nextInt();
        scanner.nextLine();


        // First, delete the user's comments directly associated with them
        String deleteDirectCommentsQuery = "DELETE FROM comment WHERE user_id = ?";
        PreparedStatement directCommentsStatement = connection.prepareStatement(deleteDirectCommentsQuery);
        directCommentsStatement.setInt(1, userId);
        directCommentsStatement.executeUpdate();

        // Then, delete the user's comments associated with their posts
        String deleteCommentsQuery = "DELETE comment FROM comment INNER JOIN post ON comment.post_id = post.id WHERE post.users_id = ?";
        PreparedStatement commentsStatement = connection.prepareStatement(deleteCommentsQuery);
        commentsStatement.setInt(1, userId);
        commentsStatement.executeUpdate();

        // Then, delete the user's posts
        String deletePostsQuery = "DELETE FROM post WHERE users_id = ?";
        PreparedStatement postsStatement = connection.prepareStatement(deletePostsQuery);
        postsStatement.setInt(1, userId);
        postsStatement.executeUpdate();

        // Finally, delete the user
        String deleteUserQuery = "DELETE FROM users WHERE users_id = ?";
        PreparedStatement userStatement = connection.prepareStatement(deleteUserQuery);
        userStatement.setInt(1, userId);
        userStatement.executeUpdate();
    }

    public static List<User> getUsersWithVisiblePosts(Connection connection) throws SQLException {
        String query = "SELECT * FROM users WHERE users_id IN (SELECT DISTINCT users_id FROM post WHERE visible = 1)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<User> usersWithVisiblePosts = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setName(resultSet.getString("name"));
            user.setEmail(resultSet.getString("email"));
            user.setPhone(resultSet.getString("phone"));
            user.setAdress(resultSet.getString("address"));
            user.setCreated(resultSet.getString("created"));
            user.setOnline(resultSet.getBoolean("online"));
            usersWithVisiblePosts.add(user);
        }
        return usersWithVisiblePosts;
    }


    public static void printUsersWithVisiblePosts(Connection connection) throws SQLException {
        List<User> users = getUsersWithVisiblePosts(connection);
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


}
