package upm.tickets.core;

public class TicketMetadata{
    private String ticketID;
    private  int MAX_PRODS_IN_TICKET;

    public TicketMetadata(String ticketID, int MAX_PROD) {
        this.ticketID = ticketID;
        this.MAX_PRODS_IN_TICKET = MAX_PROD;
    }

    public TicketMetadata(){}

    public String getTicketID() {return ticketID;}
    public int getMAX_PRODS_IN_TICKET(){return this.MAX_PRODS_IN_TICKET;}
    public void setTicketID(String ticketID) {this.ticketID = ticketID;}
}
