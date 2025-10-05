package upm;

public class Catalog {
    private final static int MAX_DIF_PRODUCTS = 200;
    private static Product[] catalog = new Product[MAX_DIF_PRODUCTS];
    private static int amountProducts = 0;

    public static void addProduct(Product product) {
        try {
            catalog[amountProducts] = product;
            amountProducts++;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Maximun products reached.");
        }
    }

    public static Product getProduct(int id) {
        int index = indexOfProduct(id);
        if (index != -1) {
            return catalog[index];
        } else {
            return null;
        }
    }


    public static Product[] getCatalog() {
        return catalog;
    }

    public static int getAmountProducts() {
        return amountProducts;
    }

    public static boolean remove(int id) { //Déjalo en boolean, puede ser util luego
        boolean removed;
        int index = indexOfProduct(id);
        if (index != -1) {
            catalog[index] = null;
            Utilities.arrayShifterToLeft(catalog);
            amountProducts--;
            removed = true;
        } else {
            removed = false;
        }
        return removed;
    }

    //busca el producto y, devuelve su índice, o -1 si no lo encuentra
    public static int indexOfProduct(int id) {
        int i = 0;
        while (i < amountProducts && catalog[i].getId() != id)
            i++;
        if (i < amountProducts)
            return i;
        else {
            return -1;
        }

    }

    //true=exist false=doesn't exist
    public static boolean idExists(int id) {
        return indexOfProduct(id) != -1;
    }

    public static boolean isEmpty(){
        return catalog[0]==null;
    }


}
