package server;

import util.ITokenProvider;
import util.ResponseCode;
import util.Token;
import util.TokenResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TokenManager extends UnicastRemoteObject implements ITokenService, ITokenProvider {

    private final Authenticator authenticator;
    private static final int TTL_MINUTES = 60;
    private final HashMap<UUID, Token> activeTokens;

    public TokenManager() throws RemoteException, NoSuchAlgorithmException {
        super();
        authenticator = new Authenticator();
        activeTokens = new HashMap<>();
    }

    @Override
    public TokenResponse generateToken(String userId, String password) {
        ResponseCode authResult = authenticator.authenticateUser(userId, password);
        Token token = null;
        if (authResult == ResponseCode.OK) {
            token = issueToken(userId);
        }
        return new TokenResponse(authResult, token);
    }

    /**
     * Method generates a single use Token
     * @param userId
     * @param password
     * @return Token
     */
    @Override
    public TokenResponse generateSingleUse(String userId, String password) {
        ResponseCode authResult = authenticator.authenticateUser(userId, password);
        Token token = null;
        if (authResult == ResponseCode.OK) {
            token = issueToken(userId);
            token.singleUse = true;
        }
        return new TokenResponse(authResult, token);
    }

    /**
     * @param token - Token object to be validated by Manager
     * @return
     */
    @Override
    public boolean validateToken(Token token) throws RemoteException {
        if (token == null) return false;
        boolean active = token.getStartTime().until(LocalDateTime.now(),
                ChronoUnit.MINUTES) < TTL_MINUTES;
        if (token.singleUse) activeTokens.remove(token.getId());
        return active && activeTokens.containsKey(token.getId());
    }

    /**
     * Method to issue a Token for newly-authenticated user
     * @param userId - String containing the username
     * @return personal generated Token
     */
    private Token issueToken(String userId) {
        Token newToken = new Token(userId);
        activeTokens.put(newToken.getId(), newToken);
        return newToken;
    }

    private void clearExpired() {
        activeTokens.entrySet().removeIf((x) -> {
            try {
                return !validateToken(x.getValue());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
