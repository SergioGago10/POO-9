package upm.tickets.core;

import upm.CLI;
import upm.products.*;
import upm.tickets.format.TicketFormatter;

import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductTicket.class, name = "product"),
        @JsonSubTypes.Type(value = ServiceTicket.class, name = "service"),
        @JsonSubTypes.Type(value = CommonTicket.class, name = "combined")
})
public abstract class Ticket<T extends Item> {
    protected List<T> itemsList = new ArrayList<>();
    private TicketMetadata ticketMetadata;
    private TicketState estado;

    public abstract TicketContent getSortedContent();

    public Ticket(String ticketID) {
        this.ticketMetadata = new TicketMetadata(ticketID);
        this.estado = TicketState.EMPTY;
    }

    public Ticket() {
        this.ticketMetadata = new TicketMetadata();
        this.estado = TicketState.EMPTY;
    }

    public TicketMetadata getTicketMetadata() { return ticketMetadata; }
    public void setTicketMetadata(TicketMetadata ticketMetadata) { this.ticketMetadata = ticketMetadata; }

    public TicketState getEstado() { return estado; }
    public void setEstado(TicketState estado) { this.estado = estado; }

    public List<T> getItemsList() {
        return Collections.unmodifiableList(itemsList);
    }

    public abstract void accept(TicketRenderer renderer);

    public boolean tryToAdd(Item item) {
        return item.addTo(this);
    }

    public boolean addSpecificProduct(Product p) { return false; }
    public boolean addSpecificService(ProductService s) { return false; }

    protected boolean internalAdd(T item) {
        if (ticketMetadata != null && this.itemsList.size() >= ticketMetadata.getMAX_PRODS_IN_TICKET()) {
            CLI.printErrorNextLine("Warning -> You can't add more products to the ticket. Try to make a new one if needed.");
            return false;
        }
        return itemsList.add(item);
    }

    public void closeTicket() {
        if (estado != TicketState.CLOSE) {
            estado = TicketState.CLOSE;
            String printed = TicketFormatter.ticketIDFinalFormat(this, LocalDateTime.now());
            if (ticketMetadata != null) {
                ticketMetadata.setPrintedID(printed);
            }
        }
    }

    public void removeProductFromTicket(String productID) {
        if (estado != TicketState.CLOSE) {
            Iterator<T> it = itemsList.iterator();
            while (it.hasNext()) {
                Item p = it.next();
                if (p.getId().equals(productID)) {
                    it.remove();
                }
            }
            if (itemsList.isEmpty()) {
                estado = TicketState.EMPTY;
            }
        } else {
            CLI.printNextLine("This ticket has been closed. You can't add or remove products from it.");
        }
    }
}
