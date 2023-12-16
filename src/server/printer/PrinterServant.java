package server.printer;

import server.acpolicy.AccessManager;
import server.Operation;
import server.ticket.TicketManager;
import util.IPrinterService;
import util.InvalidAccessException;
import util.Ticket;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements IPrinterService {

    private TicketManager ticketManager;
    private AccessManager accessManager;

    public PrinterServant(TicketManager ticketManager, AccessManager accessManager) throws RemoteException, InvalidAccessException {
        super();
        this.ticketManager = ticketManager;
        this.accessManager =  accessManager;

    }

    @Override
    public void print(String filename, String printer, Ticket ticket) throws RemoteException, InvalidAccessException {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.PRINT)) return;
        System.out.printf("|> %s called print(%s,%s)%n", ticket.username, filename, printer);
    }

    @Override
    public void queue(String printer, Ticket ticket) throws RemoteException, InvalidAccessException  {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.QUEUE)) return;
        System.out.printf("|> %s called queue(%s)%n", ticket.username, printer);
    }

    @Override
    public void topQueue(String printer, int job, Ticket ticket) throws RemoteException, InvalidAccessException  {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.TOPQUEUE)) return;
        System.out.printf("|> %s called topQueue(%s, %d)%n", ticket.username, printer, job);
    }

    @Override
    public void start(Ticket ticket) throws RemoteException, InvalidAccessException  {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.START)) return;
        System.out.printf("|> %s started printer%n", ticket.username);
    }

    @Override
    public void restart(Ticket ticket) throws RemoteException, InvalidAccessException {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.RESTART)) return;
        System.out.printf("|> %s restarted printer%n", ticket.username);
    }

    @Override
    public void stop(Ticket ticket) throws RemoteException, InvalidAccessException  {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.STOP)) return;
        System.out.printf("|> %s stopped printer%n", ticket.username);
    }

    @Override
    public void status(String printer, Ticket ticket) throws RemoteException, InvalidAccessException  {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.STATUS)) return;
        System.out.printf("|> %s called status(%s)%n", ticket.username, printer);
    }

    @Override
    public void readConfig(String parameter, Ticket ticket) throws RemoteException, InvalidAccessException  {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.READCONFIG)) return;
        System.out.printf("|> %s called readConfig(%s)%n", ticket.username, parameter);
    }

    @Override
    public void setConfig(String parameter, String value, Ticket ticket) throws RemoteException, InvalidAccessException  {
        if (!ticketManager.validateTicket(ticket)) return;
        if (!accessManager.verifyAccess(ticket.username, Operation.SETCONFIG)) return;
        System.out.printf("|> %s called setConfig(%s, %s)%n", ticket.username, parameter, value);
    }
}
