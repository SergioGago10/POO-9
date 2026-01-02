package upm.Users;

import upm.CLI;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected List<String> ticketIds;

    public User(String name, String email, String id){
        this.name = name;
        this.email = email;
        this.id=id;
        this.ticketIds = new ArrayList<>();
    }

    public String getName(){return name;}
    public String getEmail(){return email;}
    public String getId(){return id;}

    public List<Ticket<?>> getTickets() {
        TicketManager ticketManager = TicketManager.getInstance();
        List<Ticket<?>> tickets = new ArrayList<>();

        for (String id : ticketIds) {
            Ticket<?> ticket = ticketManager.getTicketById(id);
            if (ticket != null) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }


    public boolean addTicket(Ticket<?> ticket) {
        if (!ticketIds.contains(ticket.getTicketMetadata().getTicketID())) {
            ticketIds.add(ticket.getTicketMetadata().getTicketID());
            return true;
        } else {
            CLI.print("That ticket already exists.");
            return false;
        }
    }

}