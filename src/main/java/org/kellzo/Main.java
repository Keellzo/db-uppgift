package org.kellzo;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/uppgift1";
        String user = "root";
        String password = "";

        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Anslutning till databasen lyckades!");
            System.out.println();
            Statement statement = connection.createStatement();

            // User methods
//            User.addRowToUserTable(statement, scanner);
//            User.printUserTable(statement);
//            User.getOnlineUsers(statement);

            // Post methods
//             Post.addRowToPostTable(statement, scanner);
//             Post.printPostTable(statement);
//             Post.getPostByDate(statement);
//               Post.getPostsByOfflineUsersWithComments(statement);
//               Post.getQuantityOfPostsByUsers(statement);
               Post.getQuantityOfPostsByUsersFromCertainDate(statement);
            // Comment methods
//             Comment.addRowToCommentTable(statement, scanner);
//             Comment.printCommentTable(statement);

        } catch (SQLException e) {
            System.err.println("Anslutning till databasen misslyckades:");
            e.printStackTrace();
        }
    }
}
