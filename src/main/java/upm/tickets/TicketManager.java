package upm.tickets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketManager {
    // Map para acceder rápido a los tickets por su ID
    private Map<String, Ticket> tickets;
    public TicketManager() {
        tickets = new HashMap<>();
    }

    public boolean exists(String ticketId) {return tickets.containsKey(ticketId);}

    private String generateTicketId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm-");
        String datePart = now.format(fmt);
        int randomPart = new Random().nextInt(100_000); // 5 cifras
        return datePart + String.format("%05d", randomPart);
    }

    public Ticket newTicket(int cashId, int userId) {
        String ticketId = generateTicketId();
        while (exists(ticketId)) { //En caso de que exista ya esa clave (bastante raro)
            ticketId = generateTicketId();
        }
        Ticket ticket = new Ticket(ticketId, cashId, userId);
        tickets.put(ticketId, ticket);
        return ticket;
    }
    public Ticket newTicket(String ticketId, int cashId, int userId) {
       //El ticket ya tendrá el id valido, si no pues el handler se ocupará de ello.
        Ticket ticket = new Ticket(ticketId, cashId, userId);
        tickets.put(ticketId, ticket);
        return ticket;
    }

    public Ticket getTicketId(String ticketId) {
        return tickets.get(ticketId);
    }

    public boolean removeTicket(String ticketId) {
        return tickets.remove(ticketId) != null;
    }

    public void printListTickets() {
        System.out.println("Ticket list (ordered by cashID):");
        // Convertimos el map a lista y ordenamos por cashId
        List<Ticket> ticketList = new ArrayList<>(tickets.values());
        ticketList.sort(Comparator.comparingInt(Ticket::getCashId));
        for (Ticket t : ticketList) {
            System.out.println("{class: Ticket, ticketId: " + t.getTicketId() +
                    ", cashId: " + t.getCashId() +
                    ", userId: " + t.getUserId() +
                    ", closed: " + t.isClosed() + "}");
        }
    }
}


