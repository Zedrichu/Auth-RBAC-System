package server;

import server.persistence.DBManager;
import util.ResponseCode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;

class AuthenticationServant extends UnicastRemoteObject implements IAuthenticationService {
    private String user = "client";
    private String password = "password";
    private final String SESSION_TOKEN = "token";

    private DBManager dbManager;


    public AuthenticationServant() throws RemoteException {
        super();
        dbManager = DBManager.getInstance();
        dbManager.connect();
    };

    @Override
    public ResponseCode authenticate(String userId, String password, String token) {
        try {
            if (token == null) {
                // TODO: check if any rows match the query (i.e. existing user id)
                ResultSet queryResult = dbManager.executeQuery(String.format("SELECT * FROM USERS WHERE ID='%s'", userId));
                String storedPassword = queryResult.getString("PASSWORD");
                if (storedPassword.equals(password)) {
                    return ResponseCode.OK;
                } else {
                    return ResponseCode.UNAUTHORIZED;
                }
            } else if (token.equals(this.SESSION_TOKEN)) {
                return ResponseCode.OK;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return ResponseCode.FAIL;
        }
        return ResponseCode.FAIL;
//         if (token == null && user.equals(userId) && password.equals(this.password)) {
//             return ResponseCode.OK;
//         } else if (token != null && token.equals(this.SESSION_TOKEN)) {
//             return ResponseCode.OK;
//         }
//         return ResponseCode.UNAUTHORIZED;
    }
}
