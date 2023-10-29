package util;

public class TokenResponse {

    public Token token;
    public ResponseCode responseCode;

    public TokenResponse(ResponseCode responseCode, Token token) {
        this.responseCode = responseCode;
        this.token = token;
    }

}
