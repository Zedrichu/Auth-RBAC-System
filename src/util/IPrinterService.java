package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrinterService extends Remote {
    public static final String routeName = "printer";

    void print(String filename, String printer, Ticket ticket) throws RemoteException, InvalidAccessException;
    void queue(String printer, Ticket ticket) throws RemoteException, InvalidAccessException;
    void topQueue(String printer, int job, Ticket ticket) throws RemoteException, InvalidAccessException;
    void start(Ticket ticket) throws RemoteException, InvalidAccessException;
    void stop(Ticket ticket) throws RemoteException, InvalidAccessException;
    void restart(Ticket ticket) throws RemoteException, InvalidAccessException;
    void status(String printer, Ticket ticket) throws RemoteException, InvalidAccessException;
    void readConfig(String parameter, Ticket ticket) throws RemoteException, InvalidAccessException;
    void setConfig(String parameter, String value, Ticket ticket) throws RemoteException, InvalidAccessException;

}
