package upm;

import java.util.Arrays;
import java.util.Comparator;

public class Ticket {
    private final static int MAX_PRODUCTOS=100;
    private Product[] productsList;
    private int discount;
    private int amountProducts;
    private int totalCost;

    public Ticket(){
        productsList =new Product[MAX_PRODUCTOS];
        this.amountProducts=0;
        totalCost=0;
    }

    // cambiar usando amountProd
    public void addProduct(Product product){
        int i=0;
        //Esto da ArrayOutOfIndex tenedlo en cuenta al refactorizarlo
       while(this.productsList[i]!=null){
           i++;
       }
       this.productsList[i]=product;
    }

    public void sortProducts(){
        /*
        En java existen funciones internas que permiten ordenar arrays de objetos, algo muy util en nuestro caso, ya que es lo que buscamos hacer, esta function lo que hace es ordenar
        un array de objetos desde un punto de inicialización hasta un punto final, comparando algo que queramos ordenar, en nuestro caso el nombre, ya que interesa el orden alfabético.

        Es beneficioso aplicar un algoritmo ya implementado en java, ya que realizar un algoritmo de ordenación por nuestra cuenta puede ser complicado y aplicar algo como BubbleSort o similar
        no es nada eficiente en ningún caso, por lo que usamos el Arrays.sort(); el cual aplica TimSort, que da O(N*logN) un algoritmo bastante eficiente y mejor que un BubbleSort o similar, además
        este algoritmo es estable, algo que es muy importante:p

        También es posible realizar esto con la otra version de arrays.sort y hacer una estructura de datos auxiliar para que no compare nulls, pero esto es más directo y sencillo.
         Aquí dejo toda la documentación que pueda ser de utilidad

        https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#sort-T:A-int-int-java.util.Comparator- aquí está TODA la información sobre el mét.odo para saber su funcionamiento
         https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html?utm_source=chatgpt.com
         */

        Arrays.sort(productsList, 0, amountProducts, Comparator.comparing(Product::getName));
    }

    /**
     *
     * @return An array containing 3 integers: [finalPriceWithoutDiscount, finalPriceWithDiscount, totalDiscount] in that order
     */
    public int[] getTotalPriceAndDiscounts(){// debatir si calcular el precio al pedirlo o que se vaya actualizando al añadir o quitar
        int stationeryCounter=0, clothesCounter=0, bookCounter=0, electronicCounter=0;
        int finalPriceWithoutDiscount;
        int finalPriceWithDiscount = 0;
        for(int i=0; i<amountProducts; i++){
            finalPriceWithDiscount+=productsList[i].getPrice();
            switch (productsList[i].getCategory()){
                case STATIONERY:
                    stationeryCounter++;
                    break;
                case CLOTHES:
                    clothesCounter++;
                    break;
                case BOOK:
                    bookCounter++;
                    break;
                case ELECTRONIC:
                    electronicCounter++;
                    break;
                default:
                    break;
            }
        }

        finalPriceWithoutDiscount = finalPriceWithDiscount;
        if(clothesCounter>1)
            finalPriceWithDiscount= (int) (finalPriceWithDiscount*0.93);
        if(bookCounter>1)
            finalPriceWithDiscount= (int) (finalPriceWithDiscount*0.9);
        if(stationeryCounter>1)
            finalPriceWithDiscount= (int) (finalPriceWithDiscount*0.95);
        if(electronicCounter>1)
            finalPriceWithDiscount= (int) (finalPriceWithDiscount*0.97);

        int totalDiscount = finalPriceWithoutDiscount - finalPriceWithDiscount;

        return new int[]{finalPriceWithoutDiscount, finalPriceWithDiscount, totalDiscount};
    }

    public void addProductToTicket(int productID, int quantity) {
        Product productToBeAdded = Catalog.getCatalog()[productID];
        if (productToBeAdded != null) {
            boolean canAdd = true;
            for (int i = 0; (i < quantity) && (canAdd); i++) {
                if (amountProducts >= MAX_PRODUCTOS) {
                    System.err.println("You can't add more products to the ticket. Try to make a new one if needed.");
                    canAdd = false;
                } else {
                    addProduct(productToBeAdded);
                    amountProducts++;
                }
            }
        }
        printCurrentTicket();
        System.out.println("ticket add: ok");
    }


    public void printCurrentTicket(){
        sortProducts();//Antes de hacer el print, los productos deben de ir ordenados alfabeticamente con esta funcion
        for(int i=0;i<=amountProducts;i++){
            System.out.print("{class:"+this.productsList[i].getClass()
                    +", id:"+this.productsList[i].getId()+
                            ", name:"+this.productsList[i].getName()+
                    ", category:"+this.productsList[i].getCategory()+
                    ", price:"+this.productsList[i].getPrice()+ "}");
            if(areThereTwoOrMoreProductsOfThisType(this.productsList[i])){
                System.out.print("**Discount -"+(this.productsList[i].getPrice()*whatIsTheDiscountToApplyToThisProduct(this.productsList[i]))+"\n");
            }else{
                System.out.print("\n");
            }
            System.out.println("Total price: "+ getTotalPriceAndDiscounts()[0]);
            System.out.println("Total discount: "+ getTotalPriceAndDiscounts()[2]);
            System.out.println("Final price: "+ getTotalPriceAndDiscounts()[1]);
        }
    }
    private boolean areThereTwoOrMoreProductsOfThisType(Product product) {
        int counter = 0;
        for (int i = 0; i < amountProducts; i++) {
            if (productsList[i] != null && productsList[i].getCategory() == product.getCategory()) {
                counter++;
                if (counter >= 2) {
                    return true;
                }
            }
        }
        return false;
    }
    private double whatIsTheDiscountToApplyToThisProduct(Product product) {
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

    public void newTicket() {
        this.productsList = new Product[MAX_PRODUCTOS];
        this.amountProducts = 0;
        this.totalCost = 0;
        this.discount = 0;
        System.out.println("ticket new: ok");

    }

    public void removeProductFromTicket(int productID){
       int productsToRemove = 0;
        if(Catalog.idExists(productID)){
            for(int i=0;i<amountProducts;i++){
                if(this.productsList[i].getId()==productID){
                    this.productsList[i]=null;
                    arrayShifterToLeft(productsList);
                    productsToRemove++;
                }
            }
        }
        amountProducts -= productsToRemove;
    } private void arrayShifterToLeft(Product[] product){
        for (int i = 0; i < product.length - 1; i++) {
            if (product[i] == null) {
                for (int j = i; j < product.length - 1; j++) {
                    product[j] = product[j + 1];
                }
                product[product.length - 1] = null;
            }
        }
    }

}
