package server.ticket;

import util.Ticket;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface issue token
public interface ITicketValidator extends Remote {
    boolean validateTicket(Ticket ticket) throws RemoteException;
}
