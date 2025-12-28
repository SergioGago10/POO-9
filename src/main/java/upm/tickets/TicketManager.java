package upm.tickets;

import upm.Products.IProduct;
import upm.Products.Product;
import upm.Products.ProductService;

import java.util.*;

public class TicketManager {
    private Map<String, Ticket<?>> ticketsByTicketId;
    private Map<String, List<Ticket<?>>> ticketsByCashId;
    private static TicketManager instance;
    private final TicketFormatter ticketFormatter;

    private TicketManager() {
        ticketsByTicketId = new HashMap<>();
        ticketsByCashId = new HashMap<>();
        this.ticketFormatter = new TicketFormatter();
    }

    public TicketFormatter getFormatter() {return ticketFormatter;}
    public Ticket<?> getTicketById(String ticketId) {return ticketsByTicketId.get(ticketId);}
    public Map<String, Ticket<?>> getTicketsById() {return ticketsByTicketId;}
    public List<Ticket<?>> getTicketByCashId(String cashID){return ticketsByCashId.get(cashID);}

    public static TicketManager getInstance() {
        if (instance == null)
            instance=new TicketManager();
        return instance;
    }

    public boolean exists(String ticketId) {
        return ticketsByTicketId.containsKey(ticketId);
    }

    private String generateTicketId() {
        Random rand = new Random();
        int num = rand.nextInt(100000); // [0 - 99999]
        return String.format("%05d", num);
    }

    public Ticket<?> newTicket(String cashId, String userId, String option) {
        String ticketId = generateTicketId();
        while (exists(ticketId)) {
            ticketId = generateTicketId();
        }
        return newTicket(ticketId, cashId, userId,option, true);
    }

    public Ticket<?> newTicket(String ticketId, String cashId, String userId,String option, boolean isTicketIdAutoGen) {
        Ticket<? extends IProduct> ticket;
        switch (option){
            case "-c":
                ticket = new Ticket<>(ticketId, cashId, userId,TicketType.COMPOSITE,isTicketIdAutoGen);
                break;
            case "-p":
                ticket = new Ticket<Product>(ticketId, cashId, userId, TicketType.PRODUCT, isTicketIdAutoGen);
                break;
            case "-s":
                ticket = new Ticket<ProductService>(ticketId, cashId, userId, TicketType.SERVICE, isTicketIdAutoGen);
                break;
            default:
                //AQUI NUNCA VA A LLEGAR PERO ES OBLIGATORIO PARA QUE JAVA PIENSE QUE TICKET SE HA INICIALIZADO CORRECTAMENTE
                throw new IllegalArgumentException("Opción inválida: " + option);
        }
        //definimos el tipo de ticket al crearlo.
        ticketsByTicketId.put(ticketId, ticket);
        List<Ticket<?>> list = ticketsByCashId.get(cashId);
        if (list == null) {
            list = new ArrayList<>();
            ticketsByCashId.put(cashId, list);
        }
        list.add(ticket);
        System.out.println("Ticket: " + ticket.getTicketMetadata().getTicketID());
        this.getFormatter().printCurrentTicket(ticket);
        return ticket;
    }

}
