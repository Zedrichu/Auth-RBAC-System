package server;

import server.aclist.AccessController;
import server.aclist.IAccessVerifier;

import java.sql.SQLException;

public class AccessManager implements IAccessVerifier {
    private static AccessController accessController;

    public AccessManager(){
        super();
        accessController = new AccessController();
    }

    @Override
    public void init() {
        return;
    }

    @Override
    public boolean verifyAccess(String username, Operation operation) {
        return accessController.verifyAccess(username, operation);
    }


}


