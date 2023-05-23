package org.kellzo.services;

import org.kellzo.models.Comment;
import org.kellzo.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostService {
    public static void addRowToPostTable(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Skriv in ett inlägg");
        System.out.println();
        String postContent = scanner.nextLine();

        System.out.println("Enter image URL:");
        String imageUrl = scanner.nextLine();
        System.out.println("Enter image file type:");
        String fileType = scanner.nextLine();

        System.out.println("Enter user ID:");
        int userId = scanner.nextInt();
        scanner.nextLine();

        String mediaQuery = "INSERT INTO media (url, file_type) VALUES (?, ?)";
        PreparedStatement mediaStatement = connection.prepareStatement(mediaQuery, Statement.RETURN_GENERATED_KEYS);
        mediaStatement.setString(1, imageUrl);
        mediaStatement.setString(2, fileType);
        mediaStatement.executeUpdate();
        ResultSet rs = mediaStatement.getGeneratedKeys();
        int mediaId = 0;
        if(rs.next()){
            mediaId = rs.getInt(1);
        }

        String postQuery = "INSERT INTO post (post, media_id, users_id) VALUES (?, ?, ?)";
        PreparedStatement postStatement = connection.prepareStatement(postQuery);
        postStatement.setString(1, postContent);
        postStatement.setInt(2, mediaId);
        postStatement.setInt(3, userId);
        postStatement.executeUpdate();
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
                "JOIN users ON comment.user_id = users.users_id " +
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

    public static void updateUsersPost(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter post ID:");
        int postId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter new post text:");
        String newPost = scanner.nextLine();
        String query = "UPDATE post SET post = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newPost);
        preparedStatement.setInt(2, postId);
        preparedStatement.executeUpdate();
    }

    public static void deletePost(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter post ID:");
        int postId = scanner.nextInt();
        scanner.nextLine();

        // First, delete the comments associated with the post
        String deleteCommentsQuery = "DELETE FROM comment WHERE post_id = ?";
        PreparedStatement commentsStatement = connection.prepareStatement(deleteCommentsQuery);
        commentsStatement.setInt(1, postId);
        commentsStatement.executeUpdate();

        // Then, delete the post
        String deletePostQuery = "DELETE FROM post WHERE id = ?";
        PreparedStatement postStatement = connection.prepareStatement(deletePostQuery);
        postStatement.setInt(1, postId);
        postStatement.executeUpdate();
    }

    public static List<Post> getPostsWithVisibleComments(Connection connection) throws SQLException {
        String query = "SELECT * FROM post WHERE id IN (SELECT DISTINCT post_id FROM comment WHERE visible = 1)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Post> postsWithVisibleComments = new ArrayList<>();
        while (resultSet.next()) {
            Post post = new Post();
            post.setId(resultSet.getInt("id"));
            post.setPost(resultSet.getString("post"));
            post.setCreated(resultSet.getString("created"));
            post.setUsers_id(resultSet.getInt("users_id"));
            post.setVisible(resultSet.getBoolean("visible"));
            post.setComments(getCommentsByPostId(connection, post.getId()));  // assuming you have this method implemented
            postsWithVisibleComments.add(post);
        }
        return postsWithVisibleComments;
    }

    public static void printPostsWithVisibleComments(Connection connection) throws SQLException {
        List<Post> posts = getPostsWithVisibleComments(connection);
        for (Post post : posts) {
            System.out.println("Post: " + post.getPost());
            System.out.println("Visible: " + (post.isVisible() ? 1 : 0));
            for (Comment comment : post.getComments()) {
                System.out.println("\tComment: " + comment.getComment());
                System.out.println("\tVisible: " + (comment.getVisible() == 1 ? "Yes" : "No"));
                System.out.println("\t------------------");
            }
            System.out.println("--------------------");
        }
    }

    public static List<Comment> getCommentsByPostId(Connection connection, int postId) throws SQLException {
        String query = "SELECT * FROM comment WHERE post_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, postId);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Comment> comments = new ArrayList<>();
        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setId(resultSet.getInt("id"));
            comment.setComment(resultSet.getString("comment"));
            comment.setCreated(resultSet.getString("created"));
            comment.setPostId(resultSet.getInt("post_id"));
            comment.setUserId(resultSet.getInt("user_id"));
            comment.setVisible(resultSet.getInt("visible"));
            comments.add(comment);
        }
        return comments;
    }


    public static void addMediaToUserGallery(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter user ID:");
        int userId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter media ID:");
        int mediaId = scanner.nextInt();
        scanner.nextLine();

        String query = "INSERT INTO gallery (user_id, media_id) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, mediaId);
        preparedStatement.executeUpdate();
    }

    public static void printUserGallery(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter a username:");
        String userName = scanner.nextLine();

        String query = "SELECT media.url " +
                "FROM media " +
                "JOIN gallery ON media.id = gallery.media_id " +
                "JOIN users ON gallery.user_id = users.users_id " +
                "WHERE users.name = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, userName);
        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println("User " + userName + "'s gallery:");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("url"));
        }
    }








}

