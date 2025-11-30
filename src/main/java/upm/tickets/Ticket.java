package upm.tickets;

import upm.CLI;
import upm.Products.Catalog;
import upm.Products.*;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket {
    private final static int MAX_PRODUCTOS = 100;
    private List<IProduct> productsList;
    private String ticketId;
    private String cashId;
    private String userId;
    private boolean closed;
    private TicketState estado;
    private static final DateTimeFormatter TICKET_ID_FORMAT = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private List<String> customTexts; //Para saber que personalizacion tiene cada producto personalizable
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#.##");

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
    public List<IProduct> getProductsList() {
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
        productsList.sort(Comparator.comparing(IProduct::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public void addProductToTicket(String productID, int quantity, List<String> customTexts) {
        if(estado != TicketState.CLOSE){
            boolean productAdded = false;
            IProduct productToBeAdded = Catalog.getProduct(productID);
            if (productToBeAdded != null) {
                boolean canAdd = true;
                for (int i = 0; (i < quantity) && (canAdd); i++) {
                    if (productsList.size() >= MAX_PRODUCTOS) {
                        System.out.println("You can't add more products to the ticket. Try to make a new one if needed.");
                        canAdd = false;
                    }

                    else if (productToBeAdded instanceof Event) {
                        // No añadir reuniones/comidas repetidas
                        boolean alreadyInTicket = false;
                        for (IProduct p : productsList) {
                            if (p.getId().equals(productID)) {
                                alreadyInTicket = true;
                            }
                        }
                        if (alreadyInTicket) {
                            System.out.println("This product (Food/Meeting) is already in the ticket. It can not be added again.");
                            canAdd = false;
                        } else {
                            productsList.add(productToBeAdded);
                            productAdded = true;
                        }
                    }

                    else if (productToBeAdded instanceof CustomizableProduct) {
                        CustomizableProduct customProduct = (CustomizableProduct) productToBeAdded;
                        List<String> textsToAdd;
                        if (customTexts == null) {
                            // No hay textos personalizados, usamos una lista vacía
                            textsToAdd = new ArrayList<>();
                        } else {
                            // Copiamos la lista de textos pasada
                            textsToAdd = new ArrayList<>(customTexts);
                        }
                        if (textsToAdd.size() > customProduct.getMaxCustomTexts()) {
                            System.out.println("Too many custom texts for this product. Max allowed: " + customProduct.getMaxCustomTexts());
                            canAdd = false;
                        }  else {
                            // Creamos la copia del producto con precio actualizado
                            CustomizableProduct copy = new CustomizableProduct(
                                    customProduct.getId(),
                                    customProduct.getName(),
                                    customProduct.getCategory(),
                                    customProduct.getPrice(),
                                    customProduct.getMaxCustomTexts()
                            );
                            // Guardamos los textos personalizados en el copy
                            copy.setCustomTexts(textsToAdd);
                            double finalPrice = copy.calculateFinalPrice();
                            copy.setPrice(finalPrice);

                            productsList.add(copy);
                            productAdded = true;
                        }
                    }

                    else { // Producto normal (BasicProduct)
                        productsList.add(productToBeAdded);
                        productAdded = true;
                    }
                }
                if (productAdded) {
                    //Imprimimos el ticket actual despues de poner los productos
                    if(estado != TicketState.OPEN){
                        estado = TicketState.OPEN;
                    }
                    printCurrentTicket();
                    System.out.println("ticket add: ok");
                }
            } else{
                System.out.println("Product not found in catalog.");
            }
        }else {
            System.out.println("This ticket has been closed. You can't add or remove products from it.");
        }
    }


    public void printCurrentTicket() {
        CategoryDiscountCalc categoryDiscount = new CategoryDiscountCalc();
        double[] totals = categoryDiscount.calculateTotals(this);
        Map<IProduct, Double> hasDiscount = categoryDiscount.discountPerProduct(this);
        if (!productsList.isEmpty()) {
            // Ordenamos los productos por nombre antes de imprimirlos
            sortProducts();
            System.out.println("Ticket : " + this.getTicketId());
            for (IProduct currentProduct : productsList) {
                CLI.printText(currentProduct.toString());
                // Si el descuento no es igual a 1.0, el producto tiene descuento
                boolean hasAnyDiscount = (hasDiscount.get(currentProduct)!=1.0);
                if (hasAnyDiscount) {
                    //Si el discount es 1.0, significa que no tiene
                    double priceAfterDiscount = currentProduct.getPrice() * hasDiscount.get(currentProduct);
                    double discountAmount = currentProduct.getPrice() - priceAfterDiscount;
                    System.out.printf(" **Discount -%.2f%n", discountAmount);
                } else {
                    System.out.println(); //Aplicamos salto de linea si no hay descuento
                }
            }
        }
        //Segun sale en el formato, el formato es US, el punto es el que marca el decimal.
        System.out.printf(Locale.US,"\tTotal price: %s%n", PRICE_FORMAT.format(totals[0]));
        System.out.printf(Locale.US,"\tTotal discount: %s%n", PRICE_FORMAT.format(totals[2]));
        System.out.printf(Locale.US, "\tFinal price: %s%n", PRICE_FORMAT.format(totals[1]));
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
            Iterator<IProduct> it = productsList.iterator();
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
