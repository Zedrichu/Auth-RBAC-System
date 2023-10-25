package server;

import util.ResponseCode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class AuthenticationServant extends UnicastRemoteObject implements IAuthenticationService {
    private String user = "client";
    private String password = "password";
    private final String SESSION_TOKEN = "token";


    public AuthenticationServant() throws RemoteException {
        super();
    };

    @Override
    public ResponseCode authenticate(String userId, String password, String token) {
        if (token == null && user.equals(userId) && password.equals(this.password)) {
            
            return ResponseCode.OK;
        } else if (token != null && token.equals(this.SESSION_TOKEN)) {
            return ResponseCode.OK;
        } else {
            return ResponseCode.FAIL;
        }
    }
}
