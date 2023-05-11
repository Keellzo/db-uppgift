package org.kellzo;

import java.sql.*;
import java.util.Scanner;

public class Post {

    public static void addRowToPostTable(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("Skriv in ett inlägg");
        System.out.println();
        String query = "INSERT INTO post (post) VALUES ('" + scanner.nextLine() + "')";
        statement.executeUpdate(query);
    }

    public static void printPostTable(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM post");

        while (resultSet.next()) {
            System.out.println("Inlägg: " + resultSet.getString("post"));
            System.out.println("Created: " + resultSet.getString("created"));

            System.out.println("--------------------");
        }
    }

    public static void getPostByDate(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM post ORDER BY created DESC");

        while (resultSet.next()) {
            System.out.println("Inlägg: " + resultSet.getString("post"));
            System.out.println("Created: " + resultSet.getString("created"));

            System.out.println("--------------------");
        }
    }

    public static void getPostsByOfflineUsersWithComments(Statement statement) throws SQLException {
        String query = "SELECT post.*, GROUP_CONCAT(CONCAT('Name: ', users.name, ' , Comment: \"', comment.comment, '\"') SEPARATOR '\n') AS comment_details FROM post " +
                "JOIN comment ON post.id = comment.post_id " +
                "JOIN users ON comment.users_id = users.users_id " +
                "WHERE users.online = 0 " +
                "GROUP BY post.id";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println("Created: " + resultSet.getString("created"));
            System.out.println("Inlägg: " + resultSet.getString("post"));
            System.out.println("Comments:");
            System.out.println(resultSet.getString("comment_details"));
            System.out.println("--------------------");
        }
    }

    public static void getQuantityOfPostsByUsersFromCertainDate(Statement statement) throws SQLException {
        String query = "SELECT users.name, COUNT(post.id) AS quantity_of_posts FROM users " +
                "JOIN post ON users.users_id = post.users_id " +
                "WHERE post.created > '2023-05-10 15:13:25' " +
                "GROUP BY users.users_id " +
                "ORDER BY quantity_of_posts DESC";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Quantity of posts: " + resultSet.getString("quantity_of_posts"));
            System.out.println("--------------------");
        }
    }

}
