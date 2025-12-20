package upm.tickets;

import upm.CLI;
import upm.Products.ProductManager;
import upm.Products.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket<T extends IProduct>{
    private final static int MAX_PRODUCTOS = 100;
    private List<T> productsList;
    private String ticketId;
    private String cashId;
    private String userId;
    private boolean closed;
    private TicketState estado;
    private static final DateTimeFormatter TICKET_ID_FORMAT = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private List<String> customTexts; //Para saber que personalizacion tiene cada producto personalizable
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#.0######");

    public Ticket(String ticketId, String cashId, String userId, boolean isTicketIdAutoGen) {
        this.fechaApertura = LocalDateTime.now();
        this.ticketId = isTicketIdAutoGen ? fechaApertura.format(TICKET_ID_FORMAT) + "-" + ticketId : ticketId;
        this.cashId = cashId;
        this.userId = userId;
        this.closed = false;
        this.estado = TicketState.EMPTY;
        productsList = new ArrayList<>();
    }



    public String getTicketId() {return ticketId;}
    public boolean isClosed() {return closed;}
    public String getUserId() {return userId;}
    public String getCashId() {return cashId;}
    public TicketState getEstado(){return estado;}
    public List<T> getProductsList() {
        return Collections.unmodifiableList(productsList); //No queremos que se modifique el ticket, por lo que pasamos una copia solo para lectura
    }
    public List<String> getCustomTexts() {return Collections.unmodifiableList(customTexts);}

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


    public void printCurrentTicket() {
        CategoryDiscountCalc categoryDiscount = new CategoryDiscountCalc();
        double[] totals = categoryDiscount.calculateTotals(this);
        Map<Product, Double> hasDiscount = categoryDiscount.discountPerProduct(this);
        if (!productsList.isEmpty()) {
            // Ordenamos los productos por nombre antes de imprimirlos
            sortProducts();
            System.out.println("Ticket : " + this.getTicketId());
            for (Product currentProduct : productsList) {
                CLI.printText(currentProduct.toString());
                // Si el descuento no es igual a 1.0, el producto tiene descuento
                boolean hasAnyDiscount = (hasDiscount.get(currentProduct)!=1.0);
                if (hasAnyDiscount) {
                    //Si el discount es 1.0, significa que no tiene
                    double priceAfterDiscount = currentProduct.getPrice() * hasDiscount.get(currentProduct);
                    double discountAmount = currentProduct.getPrice() - priceAfterDiscount;
                    System.out.printf(" **Discount -%s%n", PRICE_FORMAT.format(discountAmount));
                } else {
                    System.out.println(); //Aplicamos salto de linea si no hay descuento
                }
            }
        }
        //Segun sale en el formato, el formato es US, el punto es el que marca el decimal.
        System.out.printf("\tTotal price: %s%n", PRICE_FORMAT.format(totals[0]));
        System.out.printf("\tTotal discount: %s%n", PRICE_FORMAT.format(totals[2]));
        System.out.printf("\tFinal price: %s%n", PRICE_FORMAT.format(totals[1]));
    }

    public void printFinalTicket() {
        if(estado != TicketState.EMPTY){
            closeTicket();
        }
        printCurrentTicket();
    }
    private void closeTicket(){
        if (estado != TicketState.CLOSE){
            estado = TicketState.CLOSE;
            fechaCierre = LocalDateTime.now();
            // Añadir fecha de cierre al ID
            ticketId = ticketId + "-" + fechaCierre.format(TICKET_ID_FORMAT);
        }
    }

    public void removeProductFromTicket(String productID) {
        if(estado != TicketState.CLOSE){
            Iterator<Product> it = productsList.iterator();
            while (it.hasNext()) {
                IProduct p = it.next();
                if (p.getId().equals(productID)) {
                    it.remove();
                }
            }
            if(productsList.isEmpty()){
                estado = TicketState.EMPTY;
            }
            this.printCurrentTicket();
        } else {
            System.out.println("This ticket has been closed. You can't add or remove products from it.");
        }
    }

}
