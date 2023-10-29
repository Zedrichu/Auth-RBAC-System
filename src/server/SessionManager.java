package server;

import util.Cookie;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SessionManager {
    private static final int ttl = 60; // Time-To-Live in minutes

    public boolean validCookie(Cookie cookie) {
        long minDiff = cookie.startTime
                .until(LocalDateTime.now(), ChronoUnit.MINUTES);
        return minDiff < ttl;
    }
}
