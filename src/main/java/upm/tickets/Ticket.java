package upm.tickets;

import upm.CLI;
import upm.Products.*;

import java.time.LocalDateTime;
import java.util.*;

public class Ticket<T extends Product>{
    private final static int MAX_PRODUCTOS = 100; // pasar luego como parametro al addmutipletimes!
    private List<T> productsList;
    private TicketMetadata ticketMetadata;
    private TicketState estado; //move
    private List<String> customTexts; //Para saber que personalizacion tiene cada producto personalizable

    public Ticket(String ticketID, String cashID, String userID, boolean isTicketIdAutoGen) {
        this.ticketMetadata = new TicketMetadata(ticketID,userID, cashID, isTicketIdAutoGen);
        this.estado = TicketState.EMPTY; // cambiar luego a ticketState y ya o hacer algo con esto
        productsList = new ArrayList<>();
    }

    public TicketMetadata getTicketMetadata() {return ticketMetadata;}
    public TicketState getEstado(){return estado;}
    public List<T> getProductsList() {
        return Collections.unmodifiableList(productsList); //No queremos que se modifique el ticket, por lo que pasamos una copia solo para lectura
    }

    //todo borrar luego no se usa
    public List<String> getCustomTexts() {return Collections.unmodifiableList(customTexts);}

    //todo borrar luego, no se usa
    public void setCustomTexts(List<String> texts) {
        if (texts == null) {
            this.customTexts = new ArrayList<>();
        } else {
            this.customTexts = new ArrayList<>(texts);
        }
    }

    public void setEstado(TicketState estado) {this.estado = estado;}

    public void sortProducts() {
        /*
         * En Java, las listas (como ArrayList) ya incluyen el .sort(),
         * este sort al igual que con los arrays, usa el algoritmo TimSort,
         * algoritmo que combina InsertionSort y MergeSort, si quieres saber más sobre el algoritmo,
         * aquí hay un video que lo explica muy bien: https://www.youtube.com/watch?v=4lKVoX6f0m8&t
         *
         * Esto permite ordenar directamente por cualquier criterio usando un Comparator
         * En nuestro caso, ordenamos por nombre alfabéticamente
         *
         * Complejidad: O(n log n)
         * Documentación oficial:
         * https://docs.oracle.com/javase/8/docs/api/java/util/List.html#sort-java.util.Comparator-
         * https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
         */
        productsList.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
    }

    /**
     * Metodo que pone un producto en el ticket, el producto a poner y sus respectivos fallos
     * son gestionados por la funcion que gestiona el comando, este metodo solamente agrega el producto al ticket
     * @param product producto a poner
     */
    public boolean addProductToTicket(T product) {
        if (productsList.size() >= MAX_PRODUCTOS) {
            return false;
        }
        productsList.add(product);
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
            Iterator<Product> it = (Iterator<Product>) productsList.iterator();
            while (it.hasNext()) {
                IProduct p = it.next();
                if (p.getId().equals(productID)) {
                    it.remove();
                }
            }
            if(productsList.isEmpty()){
                estado = TicketState.EMPTY;
            }
        } else {
            System.out.println("This ticket has been closed. You can't add or remove products from it.");
        }
    }

}
