package upm.Products;

import upm.CLI;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private final static int MAX_DIF_PRODUCTS = 200;
    public final static int MAX_CHAR_NAME = 100;
    private static List<Product> catalog = new ArrayList<>();
    private static int newId = 1;

    public static boolean addProduct(Product product) {
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

    public static Product getProduct(int id) {
        for (Product p : catalog) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static List<Product> getCatalog() {
        return catalog;
    }

    public static int getAmountProducts() {
        return catalog.size();
    }


    public static boolean remove(int id) {
        int index = -1;
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getId() == id) {
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

    public static int indexOfProduct(int id) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

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

