package upm.tickets;

public class TicketMetadata{
    private String ticketID;
    private Class<?> classType;

    public TicketMetadata(String ticketID, Class<?> classType) {
        this.ticketID = ticketID;
        this.classType = classType;
    }

    public TicketMetadata(){}

    public String getTicketID() {return ticketID;}
    public Class<?> getClassType(){return classType;}
    public void setTicketID(String ticketID) {this.ticketID = ticketID;}
}
