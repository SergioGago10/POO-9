package upm.tickets.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TicketMetadata {
    private String ticketID;
    private String printedID;
    private static final int MAX_PRODS_IN_TICKET = 100;

    public TicketMetadata(String ticketID) {
        this.ticketID = ticketID;
    }

    public TicketMetadata() {}

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getPrintedID() {
        return printedID;
    }

    public void setPrintedID(String printedID) {
        this.printedID = printedID;
    }

    @JsonIgnore
    public int getMAX_PRODS_IN_TICKET() {
        return this.MAX_PRODS_IN_TICKET;
    }
}
