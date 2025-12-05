package upm.Products;

import upm.CLI;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private final static int MAX_DIF_PRODUCTS = 200;
    public final static int MAX_CHAR_NAME = 100;
    private List<Product> catalog;
    private int newId = 1;
    private static ProductManager instance;

    private ProductManager() {
        this.catalog = new ArrayList<>();
    }

    public static ProductManager getInstance() {
        if (instance == null)
            instance = new ProductManager();
        return instance;
    }

    public boolean addProduct(Product product) {
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

    public Product getProduct(int id) {
        for (Product p : catalog) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Product> getCatalog() {
        return catalog;
    }

    public int getAmountProducts() {
        return catalog.size();
    }


    public boolean remove(int id) {
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

    public int indexOfProduct(int id) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public boolean idExists(int id) {
        return this.indexOfProduct(id) != -1;
    }

    public boolean isEmpty() {
        return catalog.isEmpty();
    }

    public int generateNewProductId() {
        while (idExists(newId))
            newId++;
        return newId;
    }
}

