package util;

import server.CryptoHasher;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Enrollment {
    private static Connection connection;
    public static void main(String[] args) throws SQLException {
        final String url = "jdbc:h2:file:./data/db_file";
        final String user = "Group85";
        final String password = "";
        final int SALT_LENGTH = 16;
        final Random RANDOM = new SecureRandom();

        DriverManager.registerDriver(new org.h2.Driver());
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to the DB!");
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS USERS"); // Potentially use BLOB instead of VARCHAR BLOB(64)===VARCHAR(128)
        statement.execute("CREATE TABLE IF NOT EXISTS USERS(ID CHAR(10) PRIMARY KEY, PASSHASH BLOB(64), SALT BLOB(16))");
        statement.close();

        List<String> usernames = new ArrayList<>(Arrays.asList("adrian", "arthur", "jeppe", "valentin"));
        List<String> passwords = new ArrayList<>(Arrays.asList("adrianPW", "arthurPW", "jeppePW", "valentinPW"));

        for (int i = 0; i < usernames.size(); i++) {
            byte[] salt = new byte[SALT_LENGTH];
            RANDOM.nextBytes(salt);
            byte[] hash = CryptoHasher.hashPassword(passwords.get(i).toCharArray(), salt);
            insertUser(usernames.get(i), hash, salt);
        }
        System.out.println("Populated the DB!");

        if (connection != null) connection.close();

    }

    private static void insertUser(String username, byte[] hash, byte[] salt) throws SQLException {
        String insertQuery = "INSERT INTO USERS VALUES(?, ?, ?)";
        PreparedStatement prepStatement = connection.prepareStatement(insertQuery);
        prepStatement.setString(1, username);
        prepStatement.setBytes(2, hash);
        prepStatement.setBytes(3, salt);
        prepStatement.execute();
        prepStatement.close();
    }

}
