package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthenticationService extends Remote {
    public String echo(String input) throws RemoteException;
}
