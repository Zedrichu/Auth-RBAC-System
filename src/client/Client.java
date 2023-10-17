package client;

import server.IAuthenticationService;
import server.IPrinterService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        IPrinterService service = (IPrinterService) Naming.lookup("rmi://localhost:8035/printer");
//        service.executeTask(new Task<>());
        service.start();
    }
}
