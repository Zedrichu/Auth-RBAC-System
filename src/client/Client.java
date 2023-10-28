package client;

import server.AppServer;
import server.IAppServer;
import server.IAuthenticationService;
import server.IPrinterService;
import util.ResponseCode;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private static final int MAX_RETRIES = 3;
    private static IPrinterService printerService;
    private static IAuthenticationService authService;
    private static String token;

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        try {
            IAppServer app = (IAppServer) Naming.lookup("rmi://localhost:8035/appserver");
            connect(app);
        } catch (RemoteException rex) {
            System.out.println("Server cannot be found. I'm quiting this");
        }
    }


    // Tries to connect client to server and handles the server response
    private static void connect(IAppServer app) throws RemoteException, NotBoundException, MalformedURLException {
        ResponseCode response = null;
        for (int i=0; i<MAX_RETRIES; i++){
            try {
                response = app.connect("client", token);
            } catch (RemoteException rex) {
                System.out.println("Server cannot be contacted. I'm quiting this");
                break;
            }

            switch (response) {
                case OK -> {
                    printerService = (IPrinterService) Naming.lookup("rmi://localhost:8035/printer");
                    return;
                }
                case UNAUTHORIZED -> {
                    authService = (IAuthenticationService) Naming.lookup("rmi://localhost:8035/authenticator");
                    authenticate();
                }
            }
        }
        if (response == ResponseCode.FAIL) {
            System.out.println("Server cannot be contacted. I'm quiting this! ");
        }
    }

    private static void authenticate() throws RemoteException {
        ResponseCode response = authService.authenticate("user123456", "password", token);
        if (response == ResponseCode.OK) {
            System.out.println("I am authenticated");
        } else {
            System.out.println("I was not authenticated");
        }
    }


}
