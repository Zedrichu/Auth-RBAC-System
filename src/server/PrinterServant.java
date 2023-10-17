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
        System.out.printf("Printed %s on printer %s", filename, printer);
    }

    @Override
    public void queue(String printer) throws RemoteException {

    }

    @Override
    public void topQueue(String printer, int job) throws RemoteException {

    }

    public void start() throws RemoteException {
        System.out.println("Printer job started");
    }

    @Override
    public void stop() throws RemoteException {
        System.out.println("Printer job stopped");
    }

    @Override
    public void status(String printer) throws RemoteException {
        System.out.println("Printer is active");
    }

    @Override
    public void readConfig(String parameter) throws RemoteException {

    }

    @Override
    public void setConfig(String parameter, String value) throws RemoteException {

    }
}
