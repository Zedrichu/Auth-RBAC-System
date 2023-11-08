package server;

public interface IAccessController {

    void init();
    boolean verifyAccess(String username, PrinterOperation op);
}
