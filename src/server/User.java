package server;


import util.Token;

public class User {
    public String username;
    public String password;
    public static Token cookie;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
