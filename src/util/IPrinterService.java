package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrinterService extends Remote {
    public static final String routeName = "printer";

    void print(String filename, String printer, Session session) throws RemoteException;
    void queue(String printer, Session session) throws RemoteException;
    void topQueue(String printer, int job, Session session) throws RemoteException;
    void start(Session session) throws RemoteException;
    void stop(Session session) throws RemoteException;
    String status(String printer, Session session) throws RemoteException;
    void readConfig(String parameter, Session session) throws RemoteException;
    void setConfig(String parameter, String value, Session session) throws RemoteException;

}
