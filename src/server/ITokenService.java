package server;

import util.Token;
import util.TokenResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface issue token
public interface ITokenService extends Remote {
    boolean validateToken(Token token);
}
