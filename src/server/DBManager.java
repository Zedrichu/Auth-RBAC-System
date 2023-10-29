package server;

import java.sql.*;

public class DBManager {

    // TODO: Remove database credentials from source code
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

    public void connect() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the DB!");
            populateDatabase();
            System.out.println("Populated the DB!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    private void populateDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS USERS");
        statement.execute("CREATE TABLE IF NOT EXISTS USERS(ID CHAR(10) PRIMARY KEY, PASSWORD VARCHAR(255))");
        statement.execute("INSERT INTO USERS VALUES('user123456', 'password');");
        statement.close();
    }

    public void disconnect() throws SQLException {
        if (connection != null) connection.close();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public int executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        int returnCode = statement.executeUpdate(query);
        statement.close();
        return returnCode;
    }

}
