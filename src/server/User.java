package server;


import util.Cookie;

public class User {
    public String username;
    public String password;
    public static Cookie cookie;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
