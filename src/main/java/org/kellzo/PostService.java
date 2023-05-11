package org.kellzo;

import java.sql.*;
import java.util.Scanner;

public class PostService {
    public static void addRowToPostTable(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Skriv in ett inlägg");
        System.out.println();
        String query = "INSERT INTO post (post) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, scanner.nextLine());
        preparedStatement.executeUpdate();
    }

    public static void printPostTable(Connection connection) throws SQLException {
        String query = "SELECT * FROM post";
        printPostsWithQuery(connection, query);
    }

    public static void getPostByDate(Connection connection) throws SQLException {
        String query = "SELECT * FROM post ORDER BY created DESC";
        printPostsWithQuery(connection, query);
    }

    private static void printPostsWithQuery(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("Inlägg: " + resultSet.getString("post"));
            System.out.println("Created: " + resultSet.getString("created"));
            System.out.println("--------------------");
        }
    }

    public static void getPostsByOfflineUsersWithComments(Connection connection) throws SQLException {
        String query = "SELECT post.*, GROUP_CONCAT(CONCAT('Name: ', users.name, ' , Comment: \"', comment.comment, '\"') SEPARATOR '\n') AS comment_details FROM post " +
                "JOIN comment ON post.id = comment.post_id " +
                "JOIN users ON comment.users_id = users.users_id " +
                "WHERE users.online = 0 " +
                "GROUP BY post.id";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("Created: " + resultSet.getString("created"));
            System.out.println("Inlägg: " + resultSet.getString("post"));
            System.out.println("Comments:");
            System.out.println(resultSet.getString("comment_details"));
            System.out.println("--------------------");
        }
    }

    public static void getQuantityOfPostsByUsersFromCertainDate(Connection connection) throws SQLException {
        String query = "SELECT users.name, COUNT(post.id) AS quantity_of_posts FROM users " +
                "JOIN post ON users.users_id = post.users_id " +
                "WHERE post.created > '2023-05-10 15:13:25' " +
                "GROUP BY users.users_id " +
                "ORDER BY quantity_of_posts DESC";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Quantity of posts: " + resultSet.getString("quantity_of_posts"));
            System.out.println("--------------------");
        }
    }
}

