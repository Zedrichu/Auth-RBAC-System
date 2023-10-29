package server;


public class User {
    public String username;
    public String password;
    public static SessionCookie cookie;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
