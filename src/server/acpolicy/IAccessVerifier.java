package server.acpolicy;

import server.Operation;

public interface IAccessVerifier {

    boolean verifyAccess(String username, Operation operation);
}
