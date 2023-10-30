package server;

import util.IPrinterService;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

public class AppServer {
    private static final int REGISTRY_PORT = 8035;
    private static IPrinterService printer;
    private static TokenManager tokenManager;
    private static Registry registry;

    public static void main(String[] args) throws RemoteException, NoSuchAlgorithmException {
        System.out.println("Initializing the RMI objects...");
        tokenManager = new TokenManager();
        printer = new PrinterServant(tokenManager);

        System.out.println("Creating RMI registry on port " + REGISTRY_PORT);
        registry = LocateRegistry.createRegistry(REGISTRY_PORT);

        System.out.println("Rebinding TokenProvider and Printer services to RMI route-names.");
        registry.rebind(tokenManager.routeName, tokenManager);
        registry.rebind(printer.routeName, printer);
    }
}
