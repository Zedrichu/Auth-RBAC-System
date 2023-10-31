package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISessionProvider extends Remote {
    public static final String routeName = "session";

    SessionResponse loginSession(String userId, String userPassword) throws RemoteException;

    SessionResponse loginSingleUse(String userId, String userPassword) throws RemoteException;
}
