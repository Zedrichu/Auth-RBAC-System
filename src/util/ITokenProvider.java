package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITokenProvider extends Remote {
    public static final String routeName = "token";

    TokenResponse generateToken(String userId, String userPassword) throws RemoteException;
}
