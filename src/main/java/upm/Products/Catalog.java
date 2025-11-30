package upm.Products;

import upm.CLI;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private final static int MAX_DIF_PRODUCTS = 200;
    public final static int MAX_CHAR_NAME = 100;
    private static List<IProduct> catalog = new ArrayList<>();
    private static int newId = 0;

    public static boolean addProduct(IProduct product) {
        boolean added = false;
        if (catalog.size() < MAX_DIF_PRODUCTS) {
            if (!idExists(product.getId())) {
                catalog.add(product);
                added = true;
            } else {
                CLI.print("Product or Event with id: " + product.getId() + " already exist.");
            }
        } else {
            CLI.print("Maximum products reached.");
        }
        return added;
    }

    public static IProduct getProduct(String id) {
        for (IProduct p : catalog) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public static List<IProduct> getCatalog() {
        return catalog;
    }

    public static int getAmountProducts() {
        return catalog.size();
    }


    public static boolean remove(String id) {
        int index = -1;
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            catalog.remove(index);
            return true;
        }
        return false;
    }

    public static int indexOfProduct(String id) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean idExists(String id) {
        return indexOfProduct(id) != -1;
    }

    public static boolean isEmpty() {
        return catalog.isEmpty();
    }

    public static String generateNewProductId() {
        while (idExists(String.valueOf(newId)))
            newId++;
        return String.valueOf(newId);
    }
}

