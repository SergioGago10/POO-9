package upm.tickets;

public class TicketMetadata{
    private String ticketID;
    private int numProducts;

    public TicketMetadata(String ticketID) {
        this.ticketID = ticketID;
        this.numProducts = 0; //siempre tenemos un ticket vacio al inicializarlo, por lo que sera 0 al inicializarlo.
    }

    public TicketMetadata(){}

    public String getTicketID() {return ticketID;}
    public void setTicketID(String ticketID) {this.ticketID = ticketID;}
    public int getNumProducts(){return numProducts;}
    public void setNumProducts(int numProducts){this.numProducts=numProducts;}
}
