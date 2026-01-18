package upm.tickets.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import upm.products.ProductService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceTicket extends Ticket<ProductService>{

    public ServiceTicket(String ticketID) {
        super(ticketID);
    }
    public ServiceTicket() {
        super();
    }

    @Override
    public void accept(TicketRenderer renderer) {
        renderer.renderPrices(this);
    }

    @JsonIgnore
    @Override
    public TicketContent getSortedContent() {
        List<ProductService> sorted = itemsList.stream()
                .sorted(Comparator.comparing(ProductService::getId))
                .collect(Collectors.toList());
        return new TicketContent(Collections.emptyList(), sorted);
    }
    @Override
    public boolean addSpecificService(ProductService s) {
        return internalAdd(s);
    }
}
