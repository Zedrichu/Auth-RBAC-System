package server;

public class AccessController implements IAccessController {

    @Override
    public void init() {
        return;
    }

    @Override
    public boolean verifyAccess(String username, PrinterOperation operation) {
        return true;
    }
}
