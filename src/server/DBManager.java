package server;

import server.aclist.AccessControlUser;

import javax.xml.transform.Result;
import java.security.SecureRandom;
import java.sql.*;
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
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, username);
        return prepStatement.executeQuery();
    }

    public void insertAccessControlUser(AccessControlUser user) throws SQLException {
        String query = "INSERT INTO ACCESS_CONTROL_USERS VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, user.username);
        prepStatement.setBoolean(2, user.print);
        prepStatement.setBoolean(3, user.queue);
        prepStatement.setBoolean(4, user.topQueue);
        prepStatement.setBoolean(5, user.start);
        prepStatement.setBoolean(6, user.stop);
        prepStatement.setBoolean(7, user.restart);
        prepStatement.setBoolean(8, user.status);
        prepStatement.setBoolean(9, user.readConfig);
        prepStatement.setBoolean(10, user.setConfig);
        prepStatement.execute();

    }

    public boolean queryUserAccess(String username, Operation operation) throws SQLException {
        String query = "SELECT ? FROM ACCESS_CONTROL_USERS WHERE USERNAME=?";
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, operation.name());
        prepStatement.setString(2, username);
        ResultSet result = prepStatement.executeQuery();
        result.next();
        return result.getBoolean(operation.name());
    }

    public void disconnect() throws SQLException {
        if (connection != null) connection.close();
    }

}
