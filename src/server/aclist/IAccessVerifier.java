package server.aclist;

import server.Operation;

import java.sql.SQLException;

public interface IAccessVerifier {

    void init();
    boolean verifyAccess(String username, Operation operation);
}
