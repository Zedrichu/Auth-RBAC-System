package util;

import java.io.Serializable;

public class TokenResponse implements Serializable {

    public Token token;
    public ResponseCode responseCode;

    public TokenResponse(ResponseCode responseCode, Token token) {
        this.responseCode = responseCode;
        this.token = token;
    }

}
