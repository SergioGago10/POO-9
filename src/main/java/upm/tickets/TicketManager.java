package upm.tickets;

import java.util.*;

public class TicketManager {
    private Map<String, Ticket> ticketsByTicketId;
    private Map<String, List<Ticket>> ticketsByCashId;
    private static TicketManager instance;
    private final TicketFormatter ticketFormatter;

    private TicketManager() {
        ticketsByTicketId = new HashMap<>();
        ticketsByCashId = new HashMap<>();
        this.ticketFormatter = new TicketFormatter();
    }

    public TicketFormatter getFormatter() {return ticketFormatter;}
    public Ticket getTicketById(String ticketId) {return ticketsByTicketId.get(ticketId);}
    public Map<String, Ticket> getTicketsById() {return ticketsByTicketId;}
    public List getTicketByCashId(String cashID){return ticketsByCashId.get(cashID);}

    public static TicketManager getInstance() {
        if (instance == null)
            instance=new TicketManager();
        return instance;
    }

    public boolean exists(String ticketId) {
        return ticketsByTicketId.containsKey(ticketId);
    }

    private String generateTicketId() {
        Random rand = new Random();
        int num = rand.nextInt(100000); // [0 - 99999]
        return String.format("%05d", num);
    }

    public Ticket newTicket(String cashId, String userId) {
        String ticketId = generateTicketId();
        while (exists(ticketId)) {
            ticketId = generateTicketId();
        }
        return newTicket(ticketId, cashId, userId, true);
    }

    public Ticket newTicket(String ticketId, String cashId, String userId, boolean isTicketIdAutoGen) {
        Ticket ticket = new Ticket(ticketId, cashId, userId, isTicketIdAutoGen);
        ticketsByTicketId.put(ticketId, ticket);
        List<Ticket> list = ticketsByCashId.get(cashId);
        if (list == null) {
            list = new ArrayList<>();
            ticketsByCashId.put(cashId, list);
        }
        list.add(ticket);
        System.out.println("Ticket: " + ticket.getTicketMetadata().getTicketID());
        this.getFormatter().printCurrentTicket(ticket);
        return ticket;
    }

    public boolean removeTicket(String ticketId) {
        Ticket ticket = ticketsByTicketId.get(ticketId);
        if (ticket == null) return false;

        List<Ticket> list = ticketsByCashId.get(ticket.getTicketMetadata().getCashID());
        if (list != null) {
            list.remove(ticket);
            if (list.isEmpty()) {
                ticketsByCashId.remove(ticket.getTicketMetadata().getCashID());
            }
        }
        ticketsByTicketId.remove(ticketId);
        return true;
    }

}
