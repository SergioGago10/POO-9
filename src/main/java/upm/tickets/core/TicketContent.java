package upm.tickets.core;

import upm.Products.Product;
import upm.Products.ProductService;
import java.util.List;

public class TicketContent {
    private final List<Product> products;
    private final List<ProductService> services;

    public TicketContent(List<Product> products, List<ProductService> services) {
        this.products = products;
        this.services = services;
    }

    public List<Product> getProducts() { return products; }
    public List<ProductService> getServices() { return services; }
}