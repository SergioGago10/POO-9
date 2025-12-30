package upm.tickets;

import java.time.LocalDateTime;

public class TicketMetadata{
    private String ticketID;
    private String cashID;
    private LocalDateTime fechaApertura;
    private int numProducts;

    public TicketMetadata(String ticketID, String cashID, boolean isTicketIDAutoGen) {
        this.fechaApertura = LocalDateTime.now();
        this.ticketID = TicketFormatter.ticketIDFormatter(isTicketIDAutoGen,ticketID,this.fechaApertura);
        this.cashID = cashID;
        this.numProducts = 0; //siempre tenemos un ticket vacio al inicializarlo, por lo que sera 0 al inicializarlo.
    }

    public String getTicketID() {return ticketID;}
    public String getCashID() {return cashID;}
    public void setTicketID(String ticketID) {this.ticketID = ticketID;}
    public int getNumProducts(){return numProducts;}
    public void setNumProducts(int numProducts){this.numProducts=numProducts;}
}
