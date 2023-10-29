package server;

import client.ITask;
import util.Cookie;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements IPrinterService {
    public PrinterServant() throws RemoteException {
        super();
    }

    @Override
    public void print(String filename, String printer, Cookie cookie) throws RemoteException {
        System.out.printf("|> Called print(%s,%s)%n", filename, printer);
    }

    @Override
    public void queue(String printer, Cookie cookie) throws RemoteException {
        System.out.printf("|> Called queue(%s)%n", printer);
    }

    @Override
    public void topQueue(String printer, int job, Cookie cookie) throws RemoteException {
        System.out.printf("|> Called topQueue(%s, %d)%n", printer, job);
    }

    public void start(Cookie cookie) throws RemoteException {
        System.out.println("|> Printer job started");
    }

    @Override
    public void stop(Cookie cookie) throws RemoteException {
        System.out.println("|> Printer job stopped");
    }

    @Override
    public void status(String printer, Cookie cookie) throws RemoteException {
        System.out.printf("|> Printer '%s' is active%n", printer);
    }

    @Override
    public void readConfig(String parameter, Cookie cookie) throws RemoteException {
        System.out.printf("|> Called readConfig(%s)%n", parameter);
    }

    @Override
    public void setConfig(String parameter, String value, Cookie cookie) throws RemoteException {
        System.out.printf("|> Called setConfig(%s)%n", parameter);
    }
}
