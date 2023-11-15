package server.session;

import util.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface issue token
public interface ISessionValidator extends Remote {
    boolean validateSession(Session session) throws RemoteException;
}
