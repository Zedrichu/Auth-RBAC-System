package server;

import client.ITask;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteComputeEngine extends Remote{
    <T> T executeTask(ITask<T> t) throws RemoteException;
}
