package upm.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import upm.products.Product;
import upm.products.ProductService;
import upm.tickets.core.Ticket;
import upm.users.Client;
import upm.users.Cash;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) //para que no se lea lo que es null (por si guardamos un JSON en el que no haya de todo)
public class SystemState {

    private List<Product> products = new ArrayList<>();
    private List<ProductService> services = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();
    private List<Cash> cash = new ArrayList<>();
    private List<Ticket<?>> tickets = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public List<ProductService> getServices() {
        return services;
    }

    public List<upm.users.Client> getClients() {
        return clients;
    }

    public List<upm.users.Cash> getCash() {
        return cash;
    }

    public List<Ticket<?>> getTickets() {
        return tickets;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setServices(List<ProductService> services) {
        this.services = services;
    }

    public void setClients(ArrayList<upm.users.Client> clients) {
        this.clients = clients;
    }

    public void setCash(ArrayList<upm.users.Cash> cash) {
        this.cash = cash;
    }

    public void setTickets(List<Ticket<?>> tickets) {
        this.tickets = tickets;
    }
}

