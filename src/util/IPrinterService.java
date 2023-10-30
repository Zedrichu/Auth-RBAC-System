package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrinterService extends Remote {
    public static final String routeName = "printer";

    void print(String filename, String printer, Token token) throws RemoteException;
    void queue(String printer, Token token) throws RemoteException;
    void topQueue(String printer, int job, Token token) throws RemoteException;
    void start(Token token) throws RemoteException;
    void stop(Token token) throws RemoteException;
    String status(String printer, Token token) throws RemoteException;
    void readConfig(String parameter, Token token) throws RemoteException;
    void setConfig(String parameter, String value, Token token) throws RemoteException;

}
