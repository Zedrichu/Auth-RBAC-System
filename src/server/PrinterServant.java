package server;

import util.IPrinterService;
import util.Session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements IPrinterService {

    private SessionManager sessionManager;

    public PrinterServant(SessionManager sessionManager) throws RemoteException {
        super();
        this.sessionManager = sessionManager;

    }

    @Override
    public void print(String filename, String printer, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> Called print(%s,%s)%n", filename, printer);

    }

    @Override
    public void queue(String printer, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> Called queue(%s)%n", printer);
    }

    @Override
    public void topQueue(String printer, int job, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> Called topQueue(%s, %d)%n", printer, job);
    }

    @Override
    public void start(Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.println("|> Printer started");
    }

    @Override
    public void restart(Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.println("|> Printer restarted");
    }

    @Override
    public void stop(Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.println("|> Printer stopped");
    }

    @Override
    public void status(String printer, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) {
            System.out.println("Session could not be verified by the server.");
            return;
        };
        System.out.printf("|> Printer '%s' is active%n", printer);
    }

    @Override
    public void readConfig(String parameter, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> Called readConfig(%s)%n", parameter);
    }

    @Override
    public void setConfig(String parameter, String value, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> Called setConfig(%s)%n", parameter);
    }
}
