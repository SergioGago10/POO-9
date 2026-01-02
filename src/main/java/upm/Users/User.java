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
    protected List<Ticket<?>> ticketList;

    public User(String name, String email, String id){
        this.name = name;
        this.email = email;
        this.id=id;
        this.ticketList = new ArrayList<>();
    }

    public String getName(){return name;}
    public String getEmail(){return email;}
    public String getId(){return id;}

    public List<Ticket<?>> getTickets() {
        return new ArrayList<>(ticketList);
    }

    public void addTicket(Ticket<?> ticket) {
        if (!ticketList.contains(ticket)) {
            ticketList.add(ticket);
        } else {
            CLI.print("That ticket already exists.");
        }
    }

}