package org.kellzo;

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
}
