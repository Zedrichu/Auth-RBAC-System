package server;

import util.ResponseCode;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Authenticator class - Handles all authentication for the DBManager
 */
class Authenticator {
    private final DBManager dbManager;

    public Authenticator() throws NoSuchAlgorithmException {
        dbManager = DBManager.getInstance();
        dbManager.connect();
    }

    public ResponseCode authenticateUser(String username, String password) {
        if (!dbManager.isConnected()) dbManager.connect();

        ResultSet queryResult;
        try {
            //#TODO Refactor into a query method in DB Manager
            String query = "SELECT PASSHASH, SALT FROM USERS WHERE ID = ?";
            PreparedStatement prepStatement = dbManager.connection.prepareStatement(query);
            prepStatement.setString(1, username);
            queryResult = prepStatement.executeQuery();
            //--------------------------------------------------

            if (!queryResult.next()) {
                return ResponseCode.INVALID_USER;
            }

            // Extract user info: HASH and SALT
            byte[] passhash = queryResult.getBytes("PASSHASH");
            byte[] salt = queryResult.getBytes("SALT");

            // Compute trial password HASH
            byte[] new_hash = CryptoHasher.hashPassword(password.toCharArray(), salt);

            // Verify salt and encrypted password instead not plain text passwords
            if (Arrays.equals(new_hash, passhash)) return ResponseCode.OK;
            return ResponseCode.UNAUTHORIZED;
        } catch (SQLException sql) {
            return ResponseCode.FAIL;
        }
    }
}
