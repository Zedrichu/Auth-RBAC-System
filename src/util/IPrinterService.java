package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrinterService extends Remote {
    public static final String routeName = "printer";

    void print(String filename, String printer, Session session) throws RemoteException, InvalidAccessException;
    void queue(String printer, Session session) throws RemoteException, InvalidAccessException;
    void topQueue(String printer, int job, Session session) throws RemoteException, InvalidAccessException;
    void start(Session session) throws RemoteException, InvalidAccessException;
    void stop(Session session) throws RemoteException, InvalidAccessException;
    void restart(Session session) throws RemoteException, InvalidAccessException;
    void status(String printer, Session session) throws RemoteException, InvalidAccessException;
    void readConfig(String parameter, Session session) throws RemoteException, InvalidAccessException;
    void setConfig(String parameter, String value, Session session) throws RemoteException, InvalidAccessException;

}
