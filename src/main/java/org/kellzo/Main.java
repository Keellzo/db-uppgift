package org.kellzo;

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

            }

        } catch (SQLException e) {
            System.err.println("Anslutning till databasen misslyckades:");
            e.printStackTrace();
        }
    }
}
