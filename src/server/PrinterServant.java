package server;

import util.IPrinterService;
import util.Token;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements IPrinterService {

    private TokenManager tokenManager;

    public PrinterServant(TokenManager tokenManager) throws RemoteException {
        super();
        this.tokenManager = tokenManager;

    }



    @Override
    public void print(String filename, String printer, Token token) throws RemoteException {
        if (!tokenManager.validateToken(token)) return;
        System.out.printf("|> Called print(%s,%s)%n", filename, printer);

    }

    @Override
    public void queue(String printer, Token token) throws RemoteException {
        if (!tokenManager.validateToken(token)) return;
        System.out.printf("|> Called queue(%s)%n", printer);
    }

    @Override
    public void topQueue(String printer, int job, Token token) throws RemoteException {
        if (!tokenManager.validateToken(token)) return;
        System.out.printf("|> Called topQueue(%s, %d)%n", printer, job);
    }

    public void start(Token token) throws RemoteException {
        if (!tokenManager.validateToken(token)) return;
        System.out.println("|> Printer job started");
    }

    @Override
    public void stop(Token token) throws RemoteException {
        if (!tokenManager.validateToken(token)) return;
        System.out.println("|> Printer job stopped");
    }

    @Override
    public String status(String printer, Token token) throws RemoteException {
        if (!tokenManager.validateToken(token)) return null;
        System.out.printf("|> Printer '%s' is active%n", printer);
        return "|> Printer is active";
    }

    @Override
    public void readConfig(String parameter, Token token) throws RemoteException {
        if (!tokenManager.validateToken(token)) return;
        System.out.printf("|> Called readConfig(%s)%n", parameter);
    }

    @Override
    public void setConfig(String parameter, String value, Token token) throws RemoteException {
        if (!tokenManager.validateToken(token)) return;
        System.out.printf("|> Called setConfig(%s)%n", parameter);
    }
}
