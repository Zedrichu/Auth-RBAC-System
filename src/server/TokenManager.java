package server;

import util.ITokenProvider;
import util.ResponseCode;
import util.Token;
import util.TokenResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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

    public TokenManager() throws RemoteException {
        super();
        authenticator = new Authenticator();
        activeTokens = new HashMap<>();
    }

    @Override
    public TokenResponse generateToken(String userId, String userPassword) {
        ResponseCode authResult = authenticator.authenticateUser(userId, userPassword);
        Token token = null;
        if (authResult == ResponseCode.OK) {
            token = issueToken(userId);
        }
        return new TokenResponse(authResult, token);
    }

    /**
     *
     * @param token - Token object to be validated by Manager
     * @return
     */
    @Override
    public boolean validateToken(Token token) {
        if (token == null) return false;
        boolean active = token.startTime.until(LocalDateTime.now(),
                ChronoUnit.MINUTES) < TTL_MINUTES;
        return active && activeTokens.containsKey(token.id);
    }

    /**
     * Method to issue a Token for newly-authenticated user
     * @param userId - String containing the username
     * @return personal generated Token
     */
    private Token issueToken(String userId) {
        Token newToken = new Token(userId);
        activeTokens.put(newToken.id, newToken);
        return newToken;
    }

    private void clearExpired() {
        activeTokens.entrySet().removeIf((x) -> !validateToken(x.getValue()));
    }

}
