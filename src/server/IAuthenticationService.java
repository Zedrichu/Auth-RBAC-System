package server;

import util.ResponseCode;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthenticationService extends Remote {
    ResponseCode authenticate(String userId, String password, String token) throws  RemoteException;
}
