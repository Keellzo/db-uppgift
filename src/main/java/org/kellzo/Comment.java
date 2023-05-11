package org.kellzo;

import java.sql.*;
import java.util.Scanner;

public class Comment {

    public static void addRowToCommentTable(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("Skriv in kommentar");
        System.out.println();
        String query = "INSERT INTO comment (comment) VALUES ('" + scanner.nextLine() + "')";
        statement.executeUpdate(query);
    }

    public static void printCommentTable(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM comment");

        while (resultSet.next()) {
            System.out.println("Comment: " + resultSet.getString("comment"));
            System.out.println("Created: " + resultSet.getString("created"));

            System.out.println("--------------------");
        }
    }
}
