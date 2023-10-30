package server;

import util.ResponseCode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


class Authenticator {
    private final DBManager dbManager;

    public Authenticator() {
        dbManager = DBManager.getInstance();
        dbManager.connect();
    }

    public ResponseCode authenticateUser(String username, String password) {
        if (!dbManager.isConnected()) dbManager.connect();

        ResultSet queryResult;
        try {
            String query = String.format("SELECT * FROM USERS WHERE ID='%s'", username);
            queryResult = dbManager.executeQuery(query);
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
