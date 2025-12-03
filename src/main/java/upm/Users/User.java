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
    protected List<Ticket> tickets;

    public User(String name, String email, String id){
        this.name = name;
        this.email = email;
        this.id=id;
        tickets=new ArrayList<>();

    }
    public String getName(){return name;}
    public String getEmail(){return email;}
    public String getId(){return id;}

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