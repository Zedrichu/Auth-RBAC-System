package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITicketProvider extends Remote {
    public static final String routeName = "session";

    TicketResponse loginSession(String userId, String userPassword) throws RemoteException;

    TicketResponse loginSingleUse(String userId, String userPassword) throws RemoteException;
}
