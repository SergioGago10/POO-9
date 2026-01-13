package upm.tickets.core;

import upm.products.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductTicket extends Ticket<Product> {
    public ProductTicket(String ticketID) {
        super(ticketID);
    }

    @Override
    public TicketContent getSortedContent() {
        List<Product> sorted = itemsList.stream()
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
        return new TicketContent(sorted, Collections.emptyList());
    }

    @Override
    public void accept(TicketRenderer renderer) {
        renderer.renderPrices(this);
    }

    @Override
    public boolean addSpecificProduct(Product p) {
        return internalAdd(p);
    }
}
