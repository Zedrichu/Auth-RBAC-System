package server;

import util.ResponseCode;

import java.sql.ResultSet;
import java.sql.SQLException;


class Authenticator {
    private final DBManager dbManager;

    public Authenticator() {
        dbManager = DBManager.getInstance();
        dbManager.connect();
    };

    public ResponseCode authenticateUser(String userId, String userPassword) {
        try (ResultSet queryResult = dbManager.executeQuery(String.format("SELECT * FROM USERS WHERE ID='%s'", userId))) {
            if (!queryResult.next()) {
                return ResponseCode.INVALID_USER;
            }
            if (queryResult.getString("PASSWORD").equals(userPassword)) return ResponseCode.OK;
            return ResponseCode.UNAUTHORIZED;
        } catch (SQLException sql) {
            return ResponseCode.FAIL;
        }
    }

}
