package upm;

import upm.Products.BasicProduct;
import upm.Products.IProduct;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private final static int MAX_DIF_PRODUCTS = 200;
    private static List<IProduct> catalog = new ArrayList<>();
    private static int newId = 0;

    public static void addProduct(IProduct product) {
        if (catalog.size() < MAX_DIF_PRODUCTS) {
            catalog.add(product);
        } else {
            System.out.println("Maximum products reached.");
        }
    }

    public static IProduct getProduct(int id) {
        int index = indexOfProduct(id);
        if (index != -1) {
            return catalog.get(index);
        } else {
            return null;
        }
    }


    public static List<IProduct> getCatalog() {
        return catalog;
    }

    public static int getAmountProducts() {
        return catalog.size();
    }


    public static boolean remove(int id) { //Déjalo en boolean, puede ser util luego
        int index = indexOfProduct(id);
        boolean removed = false;
        if (index != -1) {
            catalog.remove(index);
            removed = true;
        }
        return removed;
    }

    //busca el producto y, devuelve su índice, o -1 si no lo encuentra
    public static int indexOfProduct(int id) {
        int index = -1;
        boolean found = false;
        int i = 0;
        while (i < catalog.size() && !found) {
            if (catalog.get(i).getId() == id) {
                index = i;
                found = true;
            } else {
                i++;
            }
        }
        return index;
    }

    //true=exist false=doesn't exist
    public static boolean idExists(int id) {
        return indexOfProduct(id) != -1;
    }

    public static boolean isEmpty() {
        return catalog.isEmpty();
    }

    public static int generateNewProductId() {
        while (idExists(newId))
            newId++;
        return newId;
    }

}
