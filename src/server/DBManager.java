package server;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DBManager {
    private final int SALT_LENGTH = 16;
    public Connection connection;
    private static final Random RANDOM = new SecureRandom();

    private static DBManager dbManager = null;

    private DBManager() {}

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            connection = DriverManager
                    .getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
            System.out.println("Connected to the DB!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public ResultSet queryUserCredentials(String username) throws SQLException {
        String query = "SELECT PASSHASH, SALT FROM USERS WHERE ID = ?";
        PreparedStatement prepStatement = dbManager.connection.prepareStatement(query);
        prepStatement.setString(1, username);
        return prepStatement.executeQuery();
    }

    public void disconnect() throws SQLException {
        if (connection != null) connection.close();
    }

}
