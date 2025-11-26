package upm.tickets;

import java.util.*;

public class TicketManager {
    // Map para acceder rápido a los tickets por su ID
    private static Map<String, Ticket> ticketsByTicketId;
    private static Map<Integer,List<Ticket>> ticketsByCashId;

    public TicketManager() {
        ticketsByTicketId = new HashMap<>();
        ticketsByCashId = new HashMap<>();
    }

    public static boolean exists(String ticketId) {return ticketsByTicketId.containsKey(ticketId);}

    private static String generateTicketId() {
        Random rand = new Random();
        int num = rand.nextInt(100000); // [0 - 99999]
        return String.format("%05d", num);
    }

    //Guardamos el ticket en el ticketmanager y en el ticketCashier.
    public static Ticket newTicket(int cashId, int userId) {
        String ticketId = generateTicketId();
        while (exists(ticketId)) { //En caso de que exista ya esa clave (bastante raro)
            ticketId = generateTicketId();
        }
        return newTicket(ticketId,cashId,userId);
    }
    public static Ticket newTicket(String ticketId, int cashId, int userId) {
       //El ticket ya tendrá el id valido, si no pues el handler se ocupará de ello.
        Ticket ticket = new Ticket(ticketId, cashId, userId);
        ticketsByTicketId.put(ticketId, ticket);
        List<Ticket> list = ticketsByCashId.get(cashId);
        if (list == null) { //si es null significa que no hay ningun cashier con ese id y que tenga tickets.
            list = new ArrayList<>();
            ticketsByCashId.put(cashId, list);
        }
        list.add(ticket);
        return ticket;
    }

    public static Ticket getTicketById(String ticketId) {
        return ticketsByTicketId.get(ticketId);
    }

    //false - no se encontro el id del ticket o no existe
    //true - eliminado correctamente
    public boolean removeTicket(String ticketId) {
        Ticket ticket = ticketsByTicketId.get(ticketId);
        if (ticket == null) return false;

        List<Ticket> list = ticketsByCashId.get(ticket.getCashId());
        if (list != null) {
            list.remove(ticket);
            if (list.isEmpty()) {
                ticketsByCashId.remove(ticket.getCashId());
            }
        }
        ticketsByTicketId.remove(ticketId);
        return true;
    }

    public static void printListTickets() {
        System.out.println("Ticket list (ordered by cashID):");
        // Convertimos el map a lista y ordenamos por cashId
        List<Ticket> ticketList = new ArrayList<>(ticketsByTicketId.values());
        ticketList.sort(Comparator.comparingInt(Ticket::getCashId));
        for (Ticket t : ticketList) {
            System.out.println("{class: Ticket, ticketId: " + t.getTicketId() +
                    ", cashId: " + t.getCashId() +
                    ", userId: " + t.getUserId() +
                    ", closed: " + t.isClosed() + "}");
        }
    }

    //METODO A USAR PARA EL COMANDO CASH TICKET <TICKETID>
    public static List<Ticket> printTicketsByCashier(String cashId) {
        List<Ticket> list = ticketsByCashId.get(cashId);
        if (list == null || list.isEmpty()) {
            System.out.println("Cashier " + cashId + " has no tickets.");
        } else{
            list.sort(Comparator.comparing(Ticket::getTicketId)); //Ordenamos por ticketId
            System.out.println("Tickets for cashier " + cashId + ":");
            for (Ticket t : list) {
                System.out.println("{ticketId: " + t.getTicketId() +
                        ", userId: " + t.getUserId() +
                        ", closed: " + t.isClosed() + "}");
            }
        }
        return null;
    }
}


