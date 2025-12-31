package upm.tickets;

import java.time.LocalDateTime;

public class TicketMetadata{
    private String ticketID;
    private LocalDateTime fechaApertura;
    private int numProducts;

    public TicketMetadata(String ticketID, boolean isTicketIDAutoGen) {
        this.fechaApertura = LocalDateTime.now();
        this.ticketID = TicketFormatter.ticketIDFormatter(isTicketIDAutoGen,ticketID,this.fechaApertura);
        this.numProducts = 0; //siempre tenemos un ticket vacio al inicializarlo, por lo que sera 0 al inicializarlo.
    }

    public String getTicketID() {return ticketID;}
    public void setTicketID(String ticketID) {this.ticketID = ticketID;}
    public int getNumProducts(){return numProducts;}
    public void setNumProducts(int numProducts){this.numProducts=numProducts;}
}
