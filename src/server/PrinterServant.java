package server;

import client.ITask;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements IPrinterService {
    public PrinterServant() throws RemoteException {
        super();
    }

    @Override
    public void print(String filename, String printer) throws RemoteException {
        System.out.printf("|> Called print(%s,%s)%n", filename, printer);
    }

    @Override
    public void queue(String printer) throws RemoteException {
        System.out.printf("|> Called queue(%s)%n", printer);
    }

    @Override
    public void topQueue(String printer, int job) throws RemoteException {
        System.out.printf("|> Called topQueue(%s, %d)%n", printer, job);
    }

    public void start() throws RemoteException {
        System.out.println("|> Printer job started");
    }

    @Override
    public void stop() throws RemoteException {
        System.out.println("|> Printer job stopped");
    }

    @Override
    public void status(String printer) throws RemoteException {
        System.out.printf("|> Printer '%s' is active%n", printer);
    }

    @Override
    public void readConfig(String parameter) throws RemoteException {
        System.out.printf("|> Called readConfig(%s)%n", parameter);
    }

    @Override
    public void setConfig(String parameter, String value) throws RemoteException {
        System.out.printf("|> Called setConfig(%s)%n", parameter);
    }
}
