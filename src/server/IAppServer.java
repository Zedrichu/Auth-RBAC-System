package server;

import util.ResponseCode;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAppServer extends Remote {
    public ResponseCode connect(String userId, String token) throws RemoteException;
}
