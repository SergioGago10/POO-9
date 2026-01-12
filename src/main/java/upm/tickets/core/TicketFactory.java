package upm.tickets.core;

import upm.Products.Item;

public class TicketFactory {
    public static Ticket<? extends Item> create(String ticketId, String option) {
        return switch (option) {
            case "-p" -> new ProductTicket(ticketId);
            case "-s" -> new ServiceTicket(ticketId);
            case "-c" -> new CommonTicket(ticketId);
            default -> throw new IllegalArgumentException("Invalid option: " + option);
        };
    }
}
