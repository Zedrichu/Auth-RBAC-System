package util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Cookie {
    public UUID sessionToken;
    public String username;
    public LocalDateTime startTime;

    public Cookie(UUID token, String username) {
        this.sessionToken = token;
        this.username = username;
        this.startTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Token) {
            return this.token == ((Token) object).token;
        }
        return false;
    }

}
