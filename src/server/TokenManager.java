package server;

import util.ITokenProvider;
import util.ResponseCode;
import util.Token;
import util.TokenResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TokenManager extends UnicastRemoteObject implements ITokenService, ITokenProvider {

    private final Authenticator authenticator;
    private static final int TTL_MINUTES = 60;
    private final List<Token> activeTokens;

    public TokenManager() throws RemoteException {
        super();
        authenticator = new Authenticator();
        activeTokens = new ArrayList<>();
    }

    @Override
    public TokenResponse authenticate(String userId, String userPassword) {
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
        boolean active = token.startTime.until(LocalDateTime.now(),
                ChronoUnit.MINUTES) < TTL_MINUTES;
        return active && activeTokens.contains(token);
    }

    /**
     * Method to issue a Token for newly-authenticated user
     * @param userId - String containing the username
     * @return personal generated Token
     */
    private Token issueToken(String userId) {
        Token newToken = new Token(userId);
        activeTokens.add(newToken);
        return newToken;
    }

    private void clearInactiveTokens() {
        activeTokens.removeIf( (x) -> !validateToken(x));
    }

}
