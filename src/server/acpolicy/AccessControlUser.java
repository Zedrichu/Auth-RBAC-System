package server.acpolicy;

public class AccessControlUser {

    public String username;
    public boolean print;
    public boolean queue;
    public boolean topQueue;
    public boolean start;
    public boolean stop;
    public boolean restart;
    public boolean status;
    public boolean readConfig;
    public boolean setConfig;

    public AccessControlUser(String username) {
        this.username = username;
    }

}
