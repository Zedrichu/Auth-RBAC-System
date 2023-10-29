package server;

import util.ResponseCode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AppServer extends UnicastRemoteObject implements IAppServer {
    private static IPrinterService printerService;
    private static IAuthenticationService authenticationService;
    private static Registry registry;

    private AppServer() throws RemoteException {
        super();
        printerService = new PrinterServant();
        authenticationService = new AuthenticationServant();
    }

    public static void main(String[] args) throws RemoteException {
        registry = LocateRegistry.createRegistry(8035);
        registry.rebind("appserver", new AppServer());
    }

    public ResponseCode connect(String userID, String token) throws RemoteException{
        boolean logged_in = false;
        if (userID.equals("client") && token.equals("token")) {
            logged_in = true;
        }
        if (logged_in) {
            registry.rebind("printer", printerService);
            return ResponseCode.OK;
        } else {
            registry.rebind("authenticator", authenticationService);
            return ResponseCode.UNAUTHORIZED;
        }
    }
}
