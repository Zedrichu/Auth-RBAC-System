/*
 *   Copyright (C) 2023 Adrian Zvizdenco, Jeppe Mikkelsen, Arthur Bosquetti
 *
 *       This program is free software: you can redistribute it and/or modify it under the terms
 *       of the GNU Affero General Public License as published by the Free Software Foundation,
 *       either version 3 of the License, or (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *       without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *       See the GNU Affero General Public License for more details.
 *
 *       You should have received a copy of the GNU Affero General Public License along with
 *       this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package server.sessions;

import server.authentication.Authenticator;
import shared.*;
import util.ResponseCode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;

public class SessionManager extends UnicastRemoteObject implements ICredentialManager {

    private final Authenticator authenticator;
    private static final int TTL_MINUTES = 10;
    private final HashMap<UUID, SessionID> activeSessions;

    public SessionManager() throws RemoteException {
        super();
        authenticator = new Authenticator();
        activeSessions = new HashMap<>();
    }

    @Override
    public CredentialResponse loginSession(String userId, String password) {
        ResponseCode authResult = authenticator.authenticateUser(userId, password);
        SessionID sessionID = null;
        if (authResult == ResponseCode.OK) {
            sessionID = issueSessionID(userId, false);
        }
        return new CredentialResponse(authResult, sessionID);
    }

    /**
     * Method generates a single use Token
     * @param userId
     * @param password
     * @return Token
     */
    @Override
    public CredentialResponse loginSingleUse(String userId, String password) {
        ResponseCode authResult = authenticator.authenticateUser(userId, password);
        SessionID sessionID = null;
        if (authResult == ResponseCode.OK) {
            sessionID = issueSessionID(userId, true);
        }
        return new CredentialResponse(authResult, sessionID);
    }

    /**
     * @param authCredential - Authentication credential to be validated by Manager
     * @return
     */
    @Override
    public boolean validateCredential(IAuthCredential authCredential) throws RemoteException {
        if (authCredential == null) return false;
        clearExpired();
        boolean valid = activeSessions.containsKey(UUID.fromString(authCredential.getPayload()));
        if (authCredential.isSingleUse())
            activeSessions.remove(UUID.fromString(authCredential.getPayload()));
        return valid;
    }

    /**
     * Method to issue a session ID for newly-authenticated user
     * @param userId - String containing the username
     * @return personal generated Token
     */
    private SessionID issueSessionID(String userId, boolean singleUse) {
        SessionID newSessionID = new SessionID(userId, UUID.randomUUID(), singleUse);
        activeSessions.put(newSessionID.getId(), newSessionID);
        return newSessionID;
    }

    private void clearExpired() {
        activeSessions.entrySet().removeIf((x) ->
                x.getValue().getStartTime().until(LocalDateTime.now(),
                ChronoUnit.MINUTES) > TTL_MINUTES);
    }

}
