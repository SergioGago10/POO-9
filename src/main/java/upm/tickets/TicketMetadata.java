package upm.tickets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketMetadata{
    private String ticketID;
    private String cashID;
    private String userID;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;

    public TicketMetadata(String ticketID, String userID, String cashID, boolean isTicketIDAutoGen) {
        this.fechaApertura = LocalDateTime.now();
        this.ticketID = TicketFormatter.ticketIDFormatter(isTicketIDAutoGen,ticketID,this.fechaApertura);
        this.userID = userID;
        this.cashID = cashID;
    }

    public LocalDateTime getFechaCierre() {return fechaCierre;}
    public String getTicketID() {return ticketID;}
    public String getCashID() {return cashID;}
    public void setTicketID(String ticketID) {this.ticketID = ticketID;}

    //todo -> este no sirve de nada, borrar si al final no se usa, no queremos tener metodos vacios,
    // asi seguimos el metodo YAGNI (al igual que el userID en ticket).
    public String getUserID() {return userID;}

    public void close() {
        this.fechaCierre = LocalDateTime.now();
    }

}
