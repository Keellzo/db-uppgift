package org.kellzo;

import org.kellzo.models.Comment;
import org.kellzo.services.CommentService;
import org.kellzo.services.PostService;
import org.kellzo.services.UserService;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DBConnection.getConnection();
            if (connection != null) {
                System.out.println("Anslutning till databasen lyckades!");
                System.out.println();

//            UserService methods

//            Create user and add to log
//            UserService.toggleLogging("on");
              UserService.addRowToUserTable(connection, scanner);
//            UserService.toggleLogging("off");


//            UserService.printOnlineUsers(connection);
//            UserService.printUserTable(connection);
//            UserService.deleteUser(connection, scanner);
//            UserService.printUsersWithVisiblePosts(connection);


//            CommentService methods

//            CommentService.printCommentTable(connection);
//            CommentService.addRowToCommentTable(connection, scanner);
//            CommentService.updateUsersComment(connection, scanner);
//            CommentService.deleteComment(connection, scanner);


//            PostService methods

//            PostService.printPostTable(connection);
//            PostService.addRowToPostTable(connection, scanner);
//            PostService.getPostsByOfflineUsersWithComments(connection);
//            PostService.getQuantityOfPostsByUsersFromCertainDate(connection);
//            PostService.getPostByDate(connection);
//            PostService.updateUsersPost(connection, scanner);
//            PostService.deletePost(connection, scanner);
//            PostService.printPostsWithVisibleComments(connection);
//            PostService.addMediaToUserGallery(connection, scanner);
//            PostService.printUserGallery(connection, scanner);
            }

        } catch (SQLException e) {
            System.err.println("Anslutning till databasen misslyckades:");
            e.printStackTrace();
        }
    }
}
