package upm.users;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import upm.CLI;
import upm.products.Item;
import upm.tickets.core.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Client.class, name = "client"),
        @JsonSubTypes.Type(value = Cash.class, name = "cashier")
})
public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected List<Ticket<? extends Item>> tickets;

    public User(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.tickets = new ArrayList<>();
    }

    public User() {
        this.tickets = new ArrayList<>();
    }

    public boolean removeTicket(Ticket<? extends Item> ticket) {
        if (tickets == null || ticket == null) return false;
        return tickets.removeIf(t -> sameTicketId(t, ticket));
    }

    public void addTicket(Ticket<?> ticket) {
        if (ticket == null) {
            CLI.printErrorNextLine("Error -> Ticket is null.");
            return;
        }
        if (tickets == null) tickets = new ArrayList<>();

        boolean exists = tickets.stream().anyMatch(t -> sameTicketId(t, ticket));
        if (!exists) {
            tickets.add((Ticket<? extends Item>) ticket);
        } else {
            CLI.printErrorNextLine("Error -> That ticket already exists.");
        }
    }

    public List<Ticket<? extends Item>> getTickets() {
        if (tickets == null) tickets = new ArrayList<>();
        return tickets;
    }

    public void setTickets(List<Ticket<? extends Item>> tickets) {
        this.tickets = (tickets == null) ? new ArrayList<>() : tickets;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getId();

    private boolean sameTicketId(Ticket<? extends Item> a, Ticket<?> b) {
        if (a == null || b == null) return false;
        if (a.getTicketMetadata() == null || b.getTicketMetadata() == null) return false;
        return Objects.equals(a.getTicketMetadata().getTicketID(), b.getTicketMetadata().getTicketID());
    }
}
