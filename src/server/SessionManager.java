package server;

import util.ISessionProvider;
import util.ResponseCode;
import util.Session;
import util.SessionResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;

public class SessionManager extends UnicastRemoteObject implements ISessionValidator, ISessionProvider {

    private final Authenticator authenticator;
    private static final int TTL_MINUTES = 60;
    private final HashMap<UUID, Session> activeTokens;

    public SessionManager() throws RemoteException, NoSuchAlgorithmException {
        super();
        authenticator = new Authenticator();
        activeTokens = new HashMap<>();
    }

    @Override
    public SessionResponse loginSession(String userId, String password) {
        ResponseCode authResult = authenticator.authenticateUser(userId, password);
        Session session = null;
        if (authResult == ResponseCode.OK) {
            session = issueToken(userId);
        }
        return new SessionResponse(authResult, session);
    }

    /**
     * Method generates a single use Token
     * @param userId
     * @param password
     * @return Token
     */
    @Override
    public SessionResponse loginSingleUse(String userId, String password) {
        ResponseCode authResult = authenticator.authenticateUser(userId, password);
        Session session = null;
        if (authResult == ResponseCode.OK) {
            session = issueToken(userId);
            session.singleUse = true;
        }
        return new SessionResponse(authResult, session);
    }

    /**
     * @param session - Token object to be validated by Manager
     * @return
     */
    @Override
    public boolean validateSession(Session session) throws RemoteException {
        if (session == null) return false;
        clearExpired();
        boolean valid = activeTokens.containsKey(session.getId());
        if (session.singleUse) activeTokens.remove(session.getId());
        return valid;
    }

    /**
     * Method to issue a Token for newly-authenticated user
     * @param userId - String containing the username
     * @return personal generated Token
     */
    private Session issueToken(String userId) {
        Session newSession = new Session(userId);
        activeTokens.put(newSession.getId(), newSession);
        return newSession;
    }

    private void clearExpired() {
        activeTokens.entrySet().removeIf((x) ->
                x.getValue().getStartTime().until(LocalDateTime.now(),
                ChronoUnit.MINUTES) > TTL_MINUTES);
    }

}
