package client;

import server.IAuthenticationService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        IAuthenticationService service = (IAuthenticationService) Naming.lookup("rmi://localhost:8035/authserver");
        service.executeTask(new Task<>());
    }
}
