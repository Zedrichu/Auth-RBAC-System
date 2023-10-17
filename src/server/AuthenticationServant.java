package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationServant extends UnicastRemoteObject implements IAuthenticationService {
    public AuthenticationServant() throws RemoteException {
        super();
    };

    @Override
    public String echo(String input) throws RemoteException {
        return "Server authenticates message: " + input;
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
