package upm.tickets.core;

import upm.Products.Item;
import upm.Products.Product;
import upm.Products.ProductService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommonTicket extends Ticket<Item> {

    public CommonTicket(String ticketID) {
        super(ticketID);
    }

    @Override
    public TicketContent getSortedContent() {
        List<Product> prods = itemsList.stream()
                .filter(Product.class::isInstance)
                .map(Product.class::cast)
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        List<ProductService> servs = itemsList.stream()
                .filter(ProductService.class::isInstance)
                .map(ProductService.class::cast)
                .sorted(Comparator.comparing(ProductService::getId))
                .collect(Collectors.toList());

        return new TicketContent(prods, servs);
    }

    @Override
    public void accept(TicketRenderer renderer) {
        renderer.renderPrices(this);
    }
    @Override
    public boolean addSpecificProduct(Product p) {
        return internalAdd(p);
    }
    @Override
    public boolean addSpecificService(ProductService s) {
        return internalAdd(s);
    }
}
