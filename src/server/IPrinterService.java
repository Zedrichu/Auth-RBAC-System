package server;

import util.Cookie;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.CertPath;

public interface IPrinterService extends Remote {

    void print(String filename, String printer, Cookie cookie) throws RemoteException;
    void queue(String printer, Cookie cookie) throws RemoteException;
    void topQueue(String printer, int job, Cookie cookie) throws RemoteException;
    void start(Cookie cookie) throws RemoteException;
    void stop(Cookie cookie) throws RemoteException;
    void status(String printer, Cookie cookie) throws RemoteException;
    void readConfig(String parameter, Cookie cookie) throws RemoteException;
    void setConfig(String parameter, String value, Cookie cookie) throws RemoteException;

}
