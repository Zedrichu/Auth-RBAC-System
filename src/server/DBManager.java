package server;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private void populateDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS USERS"); // Potentially use BLOB instead of VARCHAR BLOB(64)===VARCHAR(128)
        statement.execute("CREATE TABLE IF NOT EXISTS USERS(ID CHAR(10) PRIMARY KEY, PASSHASH BLOB(64), SALT BLOB(16))");
        statement.close();

        // TODO: Could also be added through web console and removed from code (?)
        List<String> usernames = new ArrayList<>(Arrays.asList("adrian", "arthur", "jeppe", "valentin"));
        List<String> passwords = new ArrayList<>(Arrays.asList("adrianPW", "arthurPW", "jeppePW", "valentinPW"));

        for (int i = 0; i < usernames.size(); i++) {
            byte[] salt = new byte[SALT_LENGTH];
            RANDOM.nextBytes(salt);
            byte[] hash = CryptoHasher.hashPassword(passwords.get(i).toCharArray(), salt);
            insertUser(usernames.get(i), hash, salt);
        }

    }

    public ResultSet queryUserCredentials(String username) throws SQLException {
        String query = "SELECT PASSHASH, SALT FROM USERS WHERE ID = ?";
        PreparedStatement prepStatement = dbManager.connection.prepareStatement(query);
        prepStatement.setString(1, username);
        return prepStatement.executeQuery();
    }

    private void insertUser(String username, byte[] hash, byte[] salt) throws SQLException {
        String insertQuery = "INSERT INTO USERS VALUES(?, ?, ?)";
        PreparedStatement prepStatement = connection.prepareStatement(insertQuery);
        prepStatement.setString(1, username);
        prepStatement.setBytes(2, hash);
        prepStatement.setBytes(3, salt);
        prepStatement.execute();
        prepStatement.close();
    }


    public void disconnect() throws SQLException {
        if (connection != null) connection.close();
    }

}
