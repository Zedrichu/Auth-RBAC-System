package util;

import java.time.LocalDateTime;
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
}
