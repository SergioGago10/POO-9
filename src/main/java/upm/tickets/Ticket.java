package upm.tickets;

import upm.Catalog;
import upm.Products.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket {
    private final static int MAX_PRODUCTOS = 100;
    private List<IProduct> productsList;
    private String ticketId;
    private int cashId;
    private int userId;
    private boolean closed;
    private TicketState estado;
    private LocalDateTime fechaCierre;
    private List<String> customTexts; //Para saber que personalizacion tiene cada producto personalizable

    public Ticket(String ticketId, int cashId, int userId) {
        this.ticketId = ticketId;
        this.cashId = cashId;
        this.userId = userId;
        this.closed = false;
        this.estado = TicketState.EMPTY;
        productsList = new ArrayList<>();
        // El arraylist es util, ya que vamos a recorrer la lista siempre y la eliminación de productos será al inicio mitad y final,
        // por lo que una linked list no supone mucha diferencia, además que con tan pocos elementos no hay ninguna diferencia notoria entre
        // linked list y array list, es simplemente decision propia
        // Además como accedemos a elementos con un índice el arraylist es esencial en estos casos concretos, ya que una linked list no te accede directamente,
        // un arraylist sí que te puede acceder directamente al índice indicado.
        // Las linked lists serán útiles si tenemos un MAX_PRODUCTOS = 1M; o algo similar, ya que el arraylist no seria conveniente por tiempos de eliminacion de elementos
        // en esa situación sí que sería más util usar la linked list u otra estructura de datos mas avanzada (a pesar de que el acceso a elementos por indice sea mas costoso)
    }

    public String getTicketId() {return ticketId;}
    public boolean isClosed() {return closed;}
    public int getUserId() {return userId;}
    public int getCashId() {return cashId;}
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

    public void addProductToTicket(int productID, int quantity, List<String> customTexts) {
        if(estado != TicketState.CLOSED){
            boolean productAdded = false;
            IProduct productToBeAdded = Catalog.getProduct(productID);
            if (productToBeAdded != null) {
                boolean canAdd = true;
                for (int i = 0; (i < quantity) && (canAdd); i++) {
                    if (productsList.size() >= MAX_PRODUCTOS) {
                        System.out.println("You can't add more products to the ticket. Try to make a new one if needed.");
                        canAdd = false;
                    }

                    else if (productToBeAdded instanceof FoodProduct || productToBeAdded instanceof MeetingProduct) {
                        // No añadir reuniones/comidas repetidas
                        boolean alreadyInTicket = false;
                        for (IProduct p : productsList) {
                            if (p.getId() == productID) {
                                alreadyInTicket = true;
                            }
                        }
                        if (alreadyInTicket) {
                            System.out.println("This product (Meal/Meeting) is already in the ticket. It can not be added again.");
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
                    if(estado != TicketState.ACTIVE){
                        estado = TicketState.ACTIVE;
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
        if (productsList.isEmpty()) {
            System.out.println("Ticket is empty.");
        } else {
            // Ordenamos los productos por nombre antes de imprimirlos
            sortProducts();
            for (IProduct currentProduct : productsList) {
                System.out.print("{class:" + currentProduct.getClass().getSimpleName() +
                        ", id:" + currentProduct.getId() +
                        ", name:" + currentProduct.getName());
                // Solo imprimimos la categoría si es un BasicProduct
                if (currentProduct instanceof BasicProduct) {
                    BasicProduct bp = (BasicProduct) currentProduct;
                    System.out.print(", category:" + bp.getCategory());
                }
                System.out.print(", price:" + currentProduct.getPrice() + "}");
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
            System.out.printf("Total price: %.2f%n", totals[0]);
            System.out.printf("Total discount: %.2f%n", totals[2]);
            System.out.printf("Final price: %.2f%n", totals[1]);
        }
    }

    public void printFinalTicket() {
        if(estado != TicketState.EMPTY){
            closeTicket();
        }
        printCurrentTicket();
    }

    public void removeProductFromTicket(int productID) {
        if(estado != TicketState.CLOSED){
            Iterator<IProduct> it = productsList.iterator();
            while (it.hasNext()) {
                IProduct p = it.next();
                if (p.getId() == productID) {
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

    public void closeTicket(){
        if (estado != TicketState.CLOSED){
            estado = TicketState.CLOSED;
            fechaCierre = LocalDateTime.now();
            // Añadir fecha de cierre al ID
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
            ticketId = ticketId + "-" + fechaCierre.format(formatter);
        }
    }

}
