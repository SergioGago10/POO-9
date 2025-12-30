package upm.tickets;

import upm.CLI;
import upm.Products.*;
import upm.Utilities;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Ticket<T extends IProduct>{
    private final static int MAX_PRODUCTOS = 100;
    private List<T> itemsList;
    private TicketMetadata ticketMetadata;
    private TicketState estado;
    private TicketType type;

    public Ticket(String ticketID, String cashID, TicketType type ,boolean isTicketIdAutoGen) {
        this.ticketMetadata = new TicketMetadata(ticketID,cashID, isTicketIdAutoGen);
        this.estado = TicketState.EMPTY;
        itemsList = new ArrayList<>();
        this.type = type;
    }

    public TicketType getTicketType(){return this.type;}
    public TicketMetadata getTicketMetadata() {return ticketMetadata;}
    public TicketState getEstado(){return estado;}
    public List<T> getItemsList() {
        return Collections.unmodifiableList(itemsList); //No queremos que se modifique el ticket, por lo que pasamos una copia solo para lectura
    }

    public void setEstado(TicketState estado) {this.estado = estado;}

    /**
     * Metodo que pone un producto en el ticket, el producto a poner y sus respectivos fallos
     * son gestionados por la funcion que gestiona el comando, este metodo solamente agrega el producto al ticket
     * @param product producto a poner
     */
    public boolean addProductToTicket(T product) {
        if (this.ticketMetadata.getNumProducts() >= MAX_PRODUCTOS && !(product instanceof ProductService)) {
            return false;
        }
        itemsList.add(product);
        if(!(product instanceof ProductService)){
            this.ticketMetadata.setNumProducts(this.ticketMetadata.getNumProducts()+1);
        }
        return true;
    }


    public void closeTicket(){
        if (estado != TicketState.CLOSE){
            estado = TicketState.CLOSE;
            String ticketIDFinal = TicketFormatter.ticketIDFinalFormat(this,LocalDateTime.now());
            this.getTicketMetadata().setTicketID(ticketIDFinal);
        }
    }

    public void removeProductFromTicket(String productID) {
        if(estado != TicketState.CLOSE){
            Iterator<IProduct> it = (Iterator<IProduct>) itemsList.iterator();
            while (it.hasNext()) {
                IProduct p = it.next();
                if (p.getId().equals(productID)) {
                    it.remove();
                }
            }
            if(itemsList.isEmpty()){
                estado = TicketState.EMPTY;
            }
        } else {
            System.out.println("This ticket has been closed. You can't add or remove products from it.");
        }
    }

    public List<Product> getProductsSortedByName() {
        return itemsList.stream()
                .filter(p -> p instanceof Product)
                .map(p -> (Product) p)
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<ProductService> getServicesSortedById() {
        return itemsList.stream()
                .filter(p -> p instanceof ProductService)
                .map(p -> (ProductService) p)
                .sorted(Comparator.comparing(ProductService::getId))
                .collect(Collectors.toList());
    }

}
