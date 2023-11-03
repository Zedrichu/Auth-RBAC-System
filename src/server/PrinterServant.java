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
        System.out.printf("|> %s called print(%s,%s)%n", session.username, filename, printer);

    }

    @Override
    public void queue(String printer, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> %s called queue(%s)%n", session.username, printer);
    }

    @Override
    public void topQueue(String printer, int job, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> %s called topQueue(%s, %d)%n", session.username, printer, job);
    }

    @Override
    public void start(Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> %s started printer%n", session.username);
    }

    @Override
    public void restart(Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> %s restarted printer%n", session.username);
    }

    @Override
    public void stop(Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> %s stopped printer%n", session.username);
    }

    @Override
    public void status(String printer, Session session) throws RemoteException {
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> %s called status(%s)%n", session.username, printer);
    }

    @Override
    public void readConfig(String parameter, Session session) throws RemoteException {
        System.out.println("ayo");
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> %s called readConfig(%s)%n", session.username, parameter);
    }

    @Override
    public void setConfig(String parameter, String value, Session session) throws RemoteException {
        System.out.println("ayoo");
        if (!sessionManager.validateSession(session)) return;
        System.out.printf("|> %s called setConfig(%s, %s)%n", session.username, parameter, value);
    }
}
