package server.acpolicy;

import server.Operation;

public class AccessManager implements IAccessVerifier {
    private static IAccessController accessController;

    public AccessManager(){
        super();
        accessController = IAccessController.create(); //Creates either RBAC or ACL verification
    }

    @Override
    public boolean verifyAccess(String username, Operation operation) {
        return accessController.verifyAccess(username, operation);
    }


}


