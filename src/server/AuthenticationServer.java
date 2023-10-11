package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationServer extends UnicastRemoteObject implements IAuthenticationService {
    public AuthenticationServer() throws RemoteException {
        super();
    };



}
