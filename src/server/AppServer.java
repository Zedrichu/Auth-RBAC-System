package server;

import server.acpolicy.AccessManager;
import server.printer.PrinterServant;
import server.ticket.TicketManager;
import util.IPrinterService;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

public class AppServer {
    private static final int REGISTRY_PORT = 8035;
    private static IPrinterService printer;
    private static TicketManager ticketManager;
    private static AccessManager accessManager;
    private static Registry registry;

    public static void main(String[] args) throws RemoteException, NoSuchAlgorithmException {
        System.out.println("Initializing the RMI objects...");
        ticketManager = new TicketManager();
        accessManager = new AccessManager();
        printer = new PrinterServant(ticketManager, accessManager);

        System.out.println("Creating RMI registry on port " + REGISTRY_PORT);
        registry = LocateRegistry.createRegistry(REGISTRY_PORT);

        System.out.println("Rebinding SessionProvider and Printer services to RMI route-names.");
        registry.rebind(ticketManager.routeName, ticketManager);
        registry.rebind(printer.routeName, printer);

    }
}
