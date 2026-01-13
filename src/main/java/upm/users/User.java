package upm.users;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import upm.CLI;
import upm.products.Item;
import upm.tickets.core.Ticket;

import java.util.ArrayList;
import java.util.List;

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
        this.id=id;
        this.tickets = new ArrayList<>();
    }

    public User(){}

    public List<Ticket<?>> getTickets() {
        return new ArrayList<>(tickets);
    }

    public void addTicket(Ticket<?> ticket) {
        if (!tickets.contains(ticket)) {
            tickets.add(ticket);
        } else {
            CLI.print("That ticket already exists.");
        }
    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getEmail() {return email;}

    public void setTickets(List<Ticket<? extends Item>> tickets) {
        this.tickets = tickets;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}