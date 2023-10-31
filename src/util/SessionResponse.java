package util;

import java.io.Serializable;

public class SessionResponse implements Serializable {

    public Session session;
    public ResponseCode responseCode;

    public SessionResponse(ResponseCode responseCode, Session session) {
        this.responseCode = responseCode;
        this.session = session;
    }

}
