package server;

import util.IPrinterService;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

public class AppServer {
    private static final int REGISTRY_PORT = 8035;
    private static IPrinterService printer;
    private static SessionManager sessionManager;
    private static Registry registry;

    public static void main(String[] args) throws RemoteException, NoSuchAlgorithmException {
        System.out.println("Initializing the RMI objects...");
        sessionManager = new SessionManager();
        printer = new PrinterServant(sessionManager);

        System.out.println("Creating RMI registry on port " + REGISTRY_PORT);
        registry = LocateRegistry.createRegistry(REGISTRY_PORT);

        System.out.println("Rebinding SessionProvider and Printer services to RMI route-names.");
        registry.rebind(sessionManager.routeName, sessionManager);
        registry.rebind(printer.routeName, printer);
    }
}
