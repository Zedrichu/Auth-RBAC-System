package util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Token {
    public UUID id;
    public String username;
    public LocalDateTime startTime;

    public Token(String username) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.startTime = LocalDateTime.now(ZoneOffset.UTC);
    }
}
