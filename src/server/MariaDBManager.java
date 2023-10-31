package server;

// JDBC URL: jdbc:mariadb://localhost:3306/authDB

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBManager {
    private Connection connection;

    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:3306/authDB";
        String username = "";
        String password = "";

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
