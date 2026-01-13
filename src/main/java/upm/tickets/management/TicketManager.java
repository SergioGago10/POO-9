package upm.tickets.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import upm.users.Cash;
import upm.users.Client;
import upm.users.User;
import upm.users.UserManager;
import upm.Utilities;
import upm.tickets.core.TicketFactory;
import upm.tickets.core.Ticket;
import upm.tickets.format.TicketFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketManager {
    private List<Ticket<?>> ticketsList;
    private static TicketManager instance;
    private final TicketFormatter ticketFormatter;

    private TicketManager() {
        ticketsList = new ArrayList<>();
        this.ticketFormatter = new TicketFormatter();
    }

    public TicketFormatter getFormatter() {return ticketFormatter;}
    @JsonIgnore
    public List<Ticket<?>> getTicketsList(){return new ArrayList<>(ticketsList);}
    public Ticket<?> getTicketById(String ticketId) {
        return ticketsList.stream()
                .filter(t -> t.getTicketMetadata().getTicketID().equals(ticketId))
                .findFirst()
                .orElse(null);
    }

    public static TicketManager getInstance() {
        if (instance == null)
            instance=new TicketManager();
        return instance;
    }

    public boolean exists(String ticketId) {
        return ticketsList.stream().anyMatch(t -> t.getTicketMetadata().getTicketID().equals(ticketId));
    }

    public Ticket<?> newTicket(String option) {
        String ticketId = String.valueOf(Utilities.randomNumGen(5));
        while (exists(ticketId)) {
            ticketId = String.valueOf(Utilities.randomNumGen(5));
        }
        ticketId = ticketId + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));
        return newTicket(ticketId,option);
    }

    public Ticket<?> newTicket(String ticketId,String option) {
        Ticket<?> ticket = TicketFactory.create(ticketId,option);
        ticketsList.add(ticket);
        return ticket;
    }

    public void ticketCashRemover(Cash cashier){
        UserManager clientSearch = UserManager.getInstance();
        List <Ticket<?>> cashTickets = cashier.getTickets();
        for (Ticket<?> currentTicket : cashTickets) {
            this.ticketsList.remove(currentTicket); //borramos el ticket de la lista global
            Iterator<Client> clientIt = clientSearch.getClients().iterator();
            boolean found = false;
            while (!found && clientIt.hasNext()) { //borramos el ticket del cliente que lo creo
                User user = clientIt.next();
                found = user.getTickets().remove(currentTicket);
            }
        }
    }

}
