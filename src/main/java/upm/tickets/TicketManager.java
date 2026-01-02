package upm.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import upm.Products.Item;
import upm.Products.Product;
import upm.Products.ProductService;

import java.util.*;

public class TicketManager {
    private List<Ticket<?>> ticketsList;
    private static TicketManager instance;
    private final TicketFormatter ticketFormatter;

    private TicketManager() {
        ticketsList = new ArrayList<>();
        this.ticketFormatter = new TicketFormatter();
    }

    public TicketFormatter getFormatter() {return ticketFormatter;}
    @JsonIgnore
    public List<Ticket<?>> getTicketsList(){return new ArrayList<>(ticketsList);}
    public Ticket<?> getTicketById(String ticketId) {
        return ticketsList.stream()
                .filter(t -> t.getTicketMetadata().getTicketID().equals(ticketId))
                .findFirst()
                .orElse(null);
    }

    public static TicketManager getInstance() {
        if (instance == null)
            instance=new TicketManager();
        return instance;
    }

    public boolean exists(String ticketId) {
        return ticketsList.stream().anyMatch(t -> t.getTicketMetadata().getTicketID().equals(ticketId));
    }

    private String generateTicketId() {
        Random rand = new Random();
        int num = rand.nextInt(100000); // [0 - 99999]
        return String.format("%05d", num);
    }

    public Ticket<?> newTicket(String option) {
        String ticketId = generateTicketId();
        while (exists(ticketId)) {
            ticketId = generateTicketId();
        }
        return newTicket(ticketId,option, true);
    }

    public Ticket<?> newTicket(String ticketId,String option, boolean isTicketIdAutoGen) {
        Ticket<? extends Item> ticket;
        switch (option){
            case "-c":
                ticket = new Ticket<>(ticketId,TicketType.COMPOSITE,isTicketIdAutoGen);
                break;
            case "-p":
                ticket = new Ticket<Product>(ticketId, TicketType.PRODUCT, isTicketIdAutoGen);
                break;
            case "-s":
                ticket = new Ticket<ProductService>(ticketId, TicketType.SERVICE, isTicketIdAutoGen);
                break;
            default:
                //AQUI NUNCA VA A LLEGAR PERO ES OBLIGATORIO PARA QUE JAVA PIENSE QUE TICKET SE HA INICIALIZADO CORRECTAMENTE
                throw new IllegalArgumentException("Opción inválida: " + option);
        }
        //definimos el tipo de ticket al crearlo.
        ticketsList.add(ticket);
        System.out.println("Ticket: " + ticket.getTicketMetadata().getTicketID());
        return ticket;
    }

    public void addTicket(Ticket<?> t) {
        if (!ticketsList.contains(t)) {
            ticketsList.add(t);
        }
    }

    public void setTicketsList(List<Ticket<?>> ticketsList) {
        this.ticketsList.clear();
        if (ticketsList != null)
            this.ticketsList.addAll(ticketsList);
    }
}
