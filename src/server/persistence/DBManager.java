package server.persistence;

import java.sql.*;

public class DBManager {

    private final String url = "jdbc:h2:file:./data/db_file";
    private final String user = "Group85";
    private final String password = "";
    private Connection connection;

    private static DBManager dbManager = null;

    private DBManager() {}

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public boolean connect() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the DB!");
            return true;
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet;
    }

    public int executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        int returnCode = statement.executeUpdate(query);
        return returnCode;
    }

}
