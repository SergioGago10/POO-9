package upm.tickets.core;

import upm.CLI;
import upm.products.*;
import upm.tickets.format.TicketFormatter;

import java.time.LocalDateTime;
import java.util.*;

public abstract class Ticket<T extends Item>{
    private final static int MAX_PRODUCTOS = 100;
    protected List<T> itemsList = new ArrayList<>();
    private TicketMetadata ticketMetadata;
    private TicketState estado;

    public abstract TicketContent getSortedContent();

    public Ticket(String ticketID) {
        this.ticketMetadata = new TicketMetadata(ticketID, MAX_PRODUCTOS);
        this.estado = TicketState.EMPTY;
    }

    public TicketMetadata getTicketMetadata() {return ticketMetadata;}
    public TicketState getEstado(){return estado;}
    public List<T> getItemsList() {
        return Collections.unmodifiableList(itemsList); //No queremos que se modifique el ticket, por lo que pasamos una copia solo para lectura
    }

    public void setEstado(TicketState estado) {this.estado = estado;}

    public abstract void accept(TicketRenderer renderer);


    public boolean tryToAdd(Item item) {
        return item.addTo(this);
    }

    public boolean addSpecificProduct(Product p) { return false; }
    public boolean addSpecificService(ProductService s) { return false; }

    protected boolean internalAdd(T item) {
        if (this.itemsList.size() >= MAX_PRODUCTOS) return false;
        return itemsList.add(item);
    }

    public void closeTicket(){
        if (estado != TicketState.CLOSE){
            estado = TicketState.CLOSE;
            String ticketIDFinal = TicketFormatter.ticketIDFinalFormat(this,LocalDateTime.now());
            this.getTicketMetadata().setTicketID(ticketIDFinal);
        }
    }

    public void removeProductFromTicket(String productID) {
        if(estado != TicketState.CLOSE){
            Iterator<T> it = itemsList.iterator();
            while (it.hasNext()) {
                Item p = it.next();
                if (p.getId().equals(productID)) {
                    it.remove();
                }
            }
            if(itemsList.isEmpty()){
                estado = TicketState.EMPTY;
            }
        } else {
            CLI.printNextLine("This ticket has been closed. You can't add or remove products from it.");
        }
    }
}
