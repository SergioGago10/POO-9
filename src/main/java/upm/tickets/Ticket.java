package upm.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import upm.Products.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Ticket<T extends Item>{
    private final static int MAX_PRODUCTOS = 100;
    private List<T> itemsList;
    private TicketMetadata ticketMetadata;
    private TicketState estado;

    public Ticket(String ticketID, Class<T> classType) {
        this.ticketMetadata = new TicketMetadata(ticketID, classType);
        this.estado = TicketState.EMPTY;
        itemsList = Collections.checkedList(new ArrayList<>(),classType);
    }

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
    public boolean addProductToTicket(Item product) {
        if (this.itemsList.size() >= MAX_PRODUCTOS) {
            return false;
        }
        itemsList.add((T) product);
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
            Iterator<T> it = itemsList.iterator();
            while (it.hasNext()) {
                Item p = it.next();
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
    @JsonIgnore
    public List<Product> getProductsSortedByName() {
        return itemsList.stream()
                .filter(p -> p instanceof Product)
                .map(p -> (Product) p)
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<ProductService> getServicesSortedById() {
        return itemsList.stream()
                .filter(p -> p instanceof ProductService)
                .map(p -> (ProductService) p)
                .sorted(Comparator.comparing(ProductService::getId))
                .collect(Collectors.toList());
    }


    public void setItemsList(List<T> itemsList) {
        this.itemsList = itemsList;
    }

    public void setTicketMetadata(TicketMetadata ticketMetadata) {
        this.ticketMetadata = ticketMetadata;
    }
}
