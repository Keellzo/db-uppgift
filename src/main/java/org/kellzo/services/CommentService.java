package org.kellzo.services;

import java.sql.*;
import java.util.Scanner;

public class CommentService {

    public static void addRowToCommentTable(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Skriv in kommentar");
        System.out.println();
        String query = "INSERT INTO comment (comment) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, scanner.nextLine());
        preparedStatement.executeUpdate();
    }

    public static void printCommentTable(Connection connection) throws SQLException {
        String query = "SELECT * FROM comment";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("Comment: " + resultSet.getString("comment"));
            System.out.println("Created: " + resultSet.getString("created"));
            System.out.println("--------------------");
        }
    }

    public static void updateUsersComment(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter comment ID:");
        int commentId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter new comment text:");
        String newComment = scanner.nextLine();
        String query = "UPDATE comment SET comment = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newComment);
        preparedStatement.setInt(2, commentId);
        preparedStatement.executeUpdate();
    }

    public static void deleteComment(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter comment ID:");
        int commentId = scanner.nextInt();
        scanner.nextLine();
        String query = "DELETE FROM comment WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, commentId);
        preparedStatement.executeUpdate();
    }



}
