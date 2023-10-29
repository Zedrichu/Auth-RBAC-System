package util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Token {
    public UUID token;
    public String username;
    public LocalDateTime startTime;

    public Token(String username) {
        this.token = UUID.randomUUID();
        this.username = username;
        this.startTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Token) {
            return this.token == ((Token) object).token;
        }
        return false;
    }

}
