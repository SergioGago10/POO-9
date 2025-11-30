package upm.tickets;

import java.util.*;

public class TicketManager {
    private static Map<String, Ticket> ticketsByTicketId;
    private static Map<String,List<Ticket>> ticketsByCashId;

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

    public static Ticket newTicket(String cashId, String userId) {
        String ticketId = generateTicketId();
        while (exists(ticketId)) {
            ticketId = generateTicketId();
        }
        return newTicket(ticketId,cashId,userId,true);
    }
    public static Ticket newTicket(String ticketId, String cashId, String userId, boolean isTicketIdAutoGen) {
        Ticket ticket = new Ticket(ticketId, cashId, userId,isTicketIdAutoGen);
        ticketsByTicketId.put(ticketId, ticket);
        List<Ticket> list = ticketsByCashId.get(cashId);
        if (list == null) {
            list = new ArrayList<>();
            ticketsByCashId.put(cashId, list);
        }
        list.add(ticket);
        System.out.println("Ticket: " + ticket.getTicketId());
        ticket.printCurrentTicket();
        return ticket;
    }

    public static Ticket getTicketById(String ticketId) {
        return ticketsByTicketId.get(ticketId);
    }

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
        System.out.println("Ticket list : ");
        List<Ticket> ticketList = new ArrayList<>(ticketsByTicketId.values());
        ticketList.sort(Comparator.comparing(Ticket::getCashId));
        for (Ticket t : ticketList) {
            System.out.println("  " + t.getTicketId() + " - " + t.getEstado());
        }
    }


    public static List<Ticket> printTicketsByCashier(String cashId) {
        List<Ticket> list = ticketsByCashId.get(cashId);
        if (list == null || list.isEmpty()) {
            System.out.println("Cashier " + cashId + " has no tickets.");
        } else{
            list.sort(Comparator.comparing(Ticket::getTicketId));
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
