package upm.Users;

import upm.CLI;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String name;
    protected String email;
    protected List<Ticket> tickets;

    public User(String name, String email){
        this.name = name;
        this.email = email;
        tickets=new ArrayList<>();

    }
    public String getName(){return name;}
    public String getEmail(){return email;}

    public boolean addTicket(Ticket ticket){
        boolean resul;
        if(!tickets.contains(ticket)) {
            tickets.add(ticket);
            resul= true;
        }else {
            CLI.print("That ticket already exists.");
            resul= false;
        }
        return resul;
    }

}