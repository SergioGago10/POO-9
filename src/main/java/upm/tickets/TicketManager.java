package upm.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import upm.Products.Item;
import upm.Products.Product;
import upm.Products.ProductService;
import upm.Utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Ticket<?> newTicket(String option) {
        String ticketId = String.valueOf(Utilities.randomNumGen(5));
        while (exists(ticketId)) {
            ticketId = String.valueOf(Utilities.randomNumGen(5));
        }
        ticketId = ticketId + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));
        return newTicket(ticketId,option);
    }

    public Ticket<?> newTicket(String ticketId,String option) {
        Ticket<? extends Item> ticket;
        switch (option){
            case "-c":
                ticket = new Ticket<>(ticketId,TicketType.COMPOSITE);
                break;
            case "-p":
                ticket = new Ticket<Product>(ticketId, TicketType.PRODUCT);
                break;
            case "-s":
                ticket = new Ticket<ProductService>(ticketId, TicketType.SERVICE);
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
