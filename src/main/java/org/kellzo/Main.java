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

//            UserService methods

//            UserService.addRowToUserTable(connection, scanner);
//            UserService.printOnlineUsers(connection);
//            UserService.printUserTable(connection);

//            CommentService methods

//            CommentService.printCommentTable(connection);
//            CommentService.addRowToCommentTable(connection, scanner);

//            PostService methods

//            PostService.printPostTable(connection);
//            PostService.addRowToPostTable(connection, scanner);
//            PostService.getPostsByOfflineUsersWithComments(connection);
//            PostService.getQuantityOfPostsByUsersFromCertainDate(connection);
//            PostService.getPostByDate(connection);

        } catch (SQLException e) {
            System.err.println("Anslutning till databasen misslyckades:");
            e.printStackTrace();
        }
    }
}
