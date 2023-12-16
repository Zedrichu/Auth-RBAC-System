package util;

import java.io.Serializable;

public class TicketResponse implements Serializable {

    public Ticket ticket;
    public ResponseCode responseCode;

    public TicketResponse(ResponseCode responseCode, Ticket ticket) {
        this.responseCode = responseCode;
        this.ticket = ticket;
    }

}
