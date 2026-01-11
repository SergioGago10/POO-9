package upm.tickets;

import upm.Products.Item;
import upm.Products.Product;
import upm.Products.ProductService;

public class TicketFactory {
    public static Ticket<? extends Item> create(String ticketId, String option) {
        return switch (option) {
            case "-c" -> new Ticket<>(ticketId, Item.class);
            case "-p" -> new Ticket<>(ticketId, Product.class);
            case "-s" -> new Ticket<>(ticketId, ProductService.class);
            default -> throw new IllegalArgumentException("Invalid option: " + option);
        };
    }
}
