package org.example;
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

            String query = "CREATE TABLE IF NOT EXISTS comment (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), email VARCHAR(255), comment VARCHAR(255))";
            int result = statement.executeUpdate(query);

            addToCommentTable(statement, scanner);
            printCommentTable(statement);



        } catch (SQLException e) {
            System.err.println("Anslutning till databasen misslyckades:");
            e.printStackTrace();
        }
    }

    public static void addToCommentTable(Statement statement, Scanner scanner) throws SQLException {
        System.out.println("Skriv in ditt namn, email och kommentar:");
        String query2 = "INSERT INTO comment (name, email, comment) VALUES ('" + scanner.nextLine() + "', '" + scanner.nextLine() + "', '" + scanner.nextLine() + "')";
        int result2 = statement.executeUpdate(query2);
    }

    public static void printCommentTable(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM comment");

        while (resultSet.next()) {
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Email: " + resultSet.getString("email"));
            System.out.println("Comment: " + resultSet.getString("comment"));
            System.out.println("--------------------");
        }
    }

}
