package server.ticket;

import server.authentication.Authenticator;
import util.ITicketProvider;
import util.ResponseCode;
import util.Ticket;
import util.TicketResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;

public class TicketManager extends UnicastRemoteObject implements ITicketValidator, ITicketProvider {

    private final Authenticator authenticator;
    private static final int TTL_MINUTES = 60;
    private final HashMap<UUID, Ticket> activeSessions;

    public TicketManager() throws RemoteException, NoSuchAlgorithmException {
        super();
        authenticator = new Authenticator();
        activeSessions = new HashMap<>();
    }

    @Override
    public TicketResponse loginSession(String userId, String password) {
        ResponseCode authResult = authenticator.authenticateUser(userId, password);
        Ticket ticket = null;
        if (authResult == ResponseCode.OK) {
            ticket = issueToken(userId);
        }
        return new TicketResponse(authResult, ticket);
    }

    /**
     * Method generates a single use Token
     * @param userId
     * @param password
     * @return Token
     */
    @Override
    public TicketResponse loginSingleUse(String userId, String password) {
        ResponseCode authResult = authenticator.authenticateUser(userId, password);
        Ticket ticket = null;
        if (authResult == ResponseCode.OK) {
            ticket = issueToken(userId);
            ticket.singleUse = true;
        }
        return new TicketResponse(authResult, ticket);
    }

    /**
     * @param ticket - Token object to be validated by Manager
     * @return
     */
    @Override
    public boolean validateTicket(Ticket ticket) throws RemoteException {
        if (ticket == null) return false;
        clearExpired();
        boolean valid = activeSessions.containsKey(ticket.getId());
        if (ticket.singleUse) activeSessions.remove(ticket.getId());
        return valid;
    }

    /**
     * Method to issue a session Ticket for newly-authenticated user
     * @param userId - String containing the username
     * @return personal generated Token
     */
    private Ticket issueTicket(String userId) {
        Ticket newTicket = new Ticket(userId);
        activeSessions.put(newTicket.getId(), newTicket);
        return newTicket;
    }

    private void clearExpired() {
        activeSessions.entrySet().removeIf((x) ->
                x.getValue().getStartTime().until(LocalDateTime.now(),
                ChronoUnit.MINUTES) > TTL_MINUTES);
    }

}
