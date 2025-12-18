package upm.Users;


import upm.tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

public class Cash extends User {

    private List<Ticket> tickets;

    public Cash(String identifier, String name, String email) {

        super(name, email,identifier);
        this.tickets = new ArrayList<>();
    }

    @Override
    public String toString() {
        return " Cash{identifier='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}