package server;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Random;

public class DBManager {

    // TODO: Remove database credentials from source code
    private final String url = "jdbc:h2:file:./data/db_file";
    private final String user = "Group85";
    private final String password = "";
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
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the DB!");
            populateDatabase();
            System.out.println("Populated the DB!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    // Todo: Make the statement for insertion more parameterizable
    private void populateDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS USERS"); // Potentially use BLOB instead of VARCHAR BLOB(64)===VARCHAR(128)
        statement.execute("CREATE TABLE IF NOT EXISTS USERS(ID CHAR(10) PRIMARY KEY, PASSHASH BLOB(64), SALT BLOB(16))");

        // parameterized
        String username = "user1234";
        String password = "password";

        // Example of how to insert encrypted passwords in DB (SHA-512 with salt)
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        byte[] hash = CryptoHasher.hashPassword(password.toCharArray(), salt);

        //Structure: ID, PASSHASH, SALT
        String insertQuery = "INSERT INTO USERS VALUES(?, ?, ?)";
        PreparedStatement prepStatement = connection.prepareStatement(insertQuery);
        prepStatement.setString(1, username);
        prepStatement.setBytes(2, hash);
        prepStatement.setBytes(3, salt);
        prepStatement.execute();

        // Insert some values for passwords
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
