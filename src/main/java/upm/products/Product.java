package upm.products;


import upm.tickets.core.Ticket;

public abstract class Product implements Item {
    protected String id;
    protected String name;
    protected double price;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean addTo(Ticket<?> ticket) {
        // Al pasar 'this', el ticket recibe un objeto de tipo BasicProduct
        return ticket.addSpecificProduct(this);
    }
}
