package upm;

import java.util.*;

public class Ticket {
    private final static int MAX_PRODUCTOS = 100;
    private List<Product> productsList;
    private Map<Category, Boolean> hasTwoProductsInTicket;
    //poner un amountProducts es inútil, los productos que hay es el tamaño del arraylist

    public Ticket() {
        productsList = new ArrayList<>();
        // El arraylist es util, ya que vamos a recorrer la lista siempre y la eliminación de productos será al inicio mitad y final,
        // por lo que una linked list no supone mucha diferencia, además que con tan pocos elementos no hay ninguna diferencia notoria entre
        // linked list y array list, es simplemente decision propia
        // Además como accedemos a elementos con un índice el arraylist es esencial en estos casos concretos, ya que una linked list no te accede directamente,
        // un arraylist sí que te puede acceder directamente al índice indicado.
        // Las linked lists serán útiles si tenemos un MAX_PRODUCTOS = 1M; o algo similar, ya que el arraylist no seria conveniente por tiempos de eliminacion de elementos
        // en esa situación sí que sería más util usar la linked list u otra estructura de datos mas avanzada (a pesar de que el acceso a elementos por indice sea mas costoso)

        Category[] allCategories = Category.values();//Pillamos todos los enum de la clase product
        this.hasTwoProductsInTicket = new HashMap<>();

        for (Category currentCategory : allCategories) {
            hasTwoProductsInTicket.put(currentCategory, false);
        }
        //Esto lo hago por si se tiene pensado poner más enums, y ayudar a la complejidad de los algoritmos usados
        //Ya que al tener un hashmap donde tenemos las categorías y si hay más de dos productos en el ticket actual es muy rápido y eficaz de consultar para
        //poder aplicar descuentos luego
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
        productsList.sort(Comparator.comparing(Product::getName));
    }

    /**
     * Calcula el precio total y los descuentos del ticket actual.
     * El descuento se aplica a cada producto de forma individual
     * (no sobre el total final del ticket).
     * <p>
     * Complejidad: O(N)
     *
     * @return An array containing 3 integers: [finalPriceWithoutDiscount, finalPriceWithDiscount, totalDiscount] in that order
     */
    public double[] getTotalPriceAndDiscounts() {
        double finalPriceWithoutDiscount = 0;
        double finalPriceWithDiscount = 0;
        for (Product currentProduct : productsList) {
            double price = currentProduct.getPrice(); // suponemos que getPrice() devuelve int o double
            finalPriceWithoutDiscount += price;
            boolean applyDiscount = hasTwoProductsInTicket.get(currentProduct.getCategory());
            double priceAfterDiscount = price;
            if (applyDiscount) {
                double discountFactor = whatDiscountToApply(currentProduct);
                priceAfterDiscount = price * discountFactor;
            }
            finalPriceWithDiscount += priceAfterDiscount;
        }
        double totalDiscount = finalPriceWithoutDiscount - finalPriceWithDiscount;

        return new double[]{finalPriceWithoutDiscount, finalPriceWithDiscount, totalDiscount};
    }


    public void addProductToTicket(int productID, int quantity) {
        boolean productAdded = false;
        Product productToBeAdded = Catalog.getProduct(productID);
        if (productToBeAdded != null) {
            boolean canAdd = true;
            for (int i = 0; (i < quantity) && (canAdd); i++) {
                if (productsList.size() >= MAX_PRODUCTOS) {
                    System.err.println("You can't add more products to the ticket. Try to make a new one if needed.");
                    canAdd = false;
                } else {
                    productsList.add(productToBeAdded);
                    productAdded = true;
                }
            }
            if (productAdded) {
                updateHasTwo(); //Actualizamos nuestro hashmap para poner true a los elementos que tienen 2 prodcutos o más
                printCurrentTicket();
                System.out.println("ticket add: ok");
            }
        }
    }


    public void printCurrentTicket() {
        if (productsList.isEmpty()) {
            System.out.println("Ticket is empty.");
        } else {
            // Ordenamos los productos por nombre antes de imprimirlos
            sortProducts();
            for (Product currentProduct : productsList) {
                System.out.print("{class:" + currentProduct.getClass().getSimpleName() +
                        ", id:" + currentProduct.getId() +
                        ", name:" + currentProduct.getName() +
                        ", category:" + currentProduct.getCategory() +
                        ", price:" + currentProduct.getPrice() + "}");

                // Vemos si el producto está 2 o más veces en el ticket
                if (hasTwoProductsInTicket.get(currentProduct.getCategory())) {
                    double priceAfterDiscount = currentProduct.getPrice() * whatDiscountToApply(currentProduct);
                    double discountAmount = currentProduct.getPrice() - priceAfterDiscount;
                    System.out.printf(" **Discount -%.2f%n", discountAmount);
                } else {
                    System.out.println(); //Aplicamos salto de linea si no hay descuento
                }
            }
            double[] priceAndDiscounts = getTotalPriceAndDiscounts();
            System.out.printf("Total price: %.2f%n", priceAndDiscounts[0]);
            System.out.printf("Total discount: %.2f%n", priceAndDiscounts[2]);
            System.out.printf("Final price: %.2f%n", priceAndDiscounts[1]);
        }
    }

    private void updateHasTwo() {
        Category[] allCategories = Category.values();
        //Creamos un HashMap donde guardamos la categoria(enum) y el numero de apariciones que tiene
        HashMap<Category, Integer> countMap = new HashMap<>();

        //Ponemos todos sus valores correspondientes, las apariciones a 0 y luego los enums existentes
        for (Category allCategory : allCategories) {
            countMap.put(allCategory, 0);
        }

        //Recorremos todos los productos y contamos las apariciones de cada producto, actualizándolo en el hashmap
        for (Product p : productsList) {
            Category cat = p.getCategory();
            countMap.put(cat, countMap.get(cat) + 1);
        }

        //Actualizamos el HashMap hasTwo según el conteo
        for (Category cat : allCategories) {
            hasTwoProductsInTicket.put(cat, countMap.get(cat) >= 2);
        }
    }

    private double whatDiscountToApply(Product product) {
        double discount;
        switch (product.getCategory()) {
            case STATIONERY:
                discount = 0.95;
                break;
            case CLOTHES:
                discount = 0.93;
                break;
            case BOOK:
                discount = 0.9;
                break;
            case ELECTRONIC:
                discount = 0.97;
                break;
            default:
                discount = 1;
                break;
        }
        return discount;
    }

    public void removeProductFromTicket(int productID) {
        //Usamos un iterator, ya que en la eliminacion es lo eficiente y lo que se debe hacer, un for loop con ajuste de índice sería incorrecto y mala práctica
        Iterator<Product> it = productsList.iterator();
        while (it.hasNext()) {
            Product p = it.next();
            if (p.getId() == productID) {
                it.remove();
            }
        }
        updateHasTwo(); //Actualizamos nuestro hashmap para poner false a los elementos que no tienen 2 productos
    }

    public void ticketNew() {
        productsList.clear();

        for (Category cat : Category.values()) {
            hasTwoProductsInTicket.put(cat, false);
        }

        System.out.println("ticket  new:  ok");
    }




}
