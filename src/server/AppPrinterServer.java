package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class AppPrinterServer {
    public static void main(String[] args) throws RemoteException {
        LocateRegistry.createRegistry(8035);
    }
}
