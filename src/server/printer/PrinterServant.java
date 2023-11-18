package server.printer;

import server.acpolicy.AccessManager;
import server.Operation;
import server.session.SessionManager;
import util.IPrinterService;
import util.InvalidAccessException;
import util.Session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements IPrinterService {

    private SessionManager sessionManager;
    private AccessManager accessManager;

    public PrinterServant(SessionManager sessionManager, AccessManager accessManager) throws RemoteException, InvalidAccessException {
        super();
        this.sessionManager = sessionManager;
        this.accessManager =  accessManager;

    }

    @Override
    public void print(String filename, String printer, Session session) throws RemoteException, InvalidAccessException {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.PRINT)) return;
        System.out.printf("|> %s called print(%s,%s)%n", session.username, filename, printer);
    }

    @Override
    public void queue(String printer, Session session) throws RemoteException, InvalidAccessException  {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.QUEUE)) return;
        System.out.printf("|> %s called queue(%s)%n", session.username, printer);
    }

    @Override
    public void topQueue(String printer, int job, Session session) throws RemoteException, InvalidAccessException  {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.TOPQUEUE)) return;
        System.out.printf("|> %s called topQueue(%s, %d)%n", session.username, printer, job);
    }

    @Override
    public void start(Session session) throws RemoteException, InvalidAccessException  {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.START)) return;
        System.out.printf("|> %s started printer%n", session.username);
    }

    @Override
    public void restart(Session session) throws RemoteException, InvalidAccessException {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.RESTART)) return;
        System.out.printf("|> %s restarted printer%n", session.username);
    }

    @Override
    public void stop(Session session) throws RemoteException, InvalidAccessException  {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.STOP)) return;
        System.out.printf("|> %s stopped printer%n", session.username);
    }

    @Override
    public void status(String printer, Session session) throws RemoteException, InvalidAccessException  {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.STATUS)) return;
        System.out.printf("|> %s called status(%s)%n", session.username, printer);
    }

    @Override
    public void readConfig(String parameter, Session session) throws RemoteException, InvalidAccessException  {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.READCONFIG)) return;
        System.out.printf("|> %s called readConfig(%s)%n", session.username, parameter);
    }

    @Override
    public void setConfig(String parameter, String value, Session session) throws RemoteException, InvalidAccessException  {
        if (!sessionManager.validateSession(session)) return;
        if (!accessManager.verifyAccess(session.username, Operation.SETCONFIG)) return;
        System.out.printf("|> %s called setConfig(%s, %s)%n", session.username, parameter, value);
    }
}
