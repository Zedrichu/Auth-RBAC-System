package util;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Token implements Serializable {
    private UUID id;
    public String username;
    private LocalDateTime startTime;
    public boolean singleUse = false;

    public Token(String username) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.startTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
