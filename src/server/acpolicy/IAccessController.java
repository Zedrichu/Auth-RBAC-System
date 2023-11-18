package server.acpolicy;

import server.Operation;

public interface IAccessController {
    static IAccessController create() {
        return new ACLController();
    }

    public boolean grantAccess(String username, Operation operation);

}
