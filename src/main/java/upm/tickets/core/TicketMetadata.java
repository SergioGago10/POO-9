package upm.tickets.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TicketMetadata{
    private String ticketID;
    private static final int MAX_PRODS_IN_TICKET=100;

    public TicketMetadata(String ticketID) {
        this.ticketID = ticketID;
    }

    public TicketMetadata(){}

    public String getTicketID() {return ticketID;}

    @JsonIgnore
    public int getMAX_PRODS_IN_TICKET(){return this.MAX_PRODS_IN_TICKET;}
    public void setTicketID(String ticketID) {this.ticketID = ticketID;}
}
