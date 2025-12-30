package upm.Products;

import upm.CLI;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private final static int MAX_DIF_PRODUCTS = 200;
    public final static int MAX_CHAR_NAME = 100;
    private List<Product> catalogProducts;
    private List<ProductService> catalogServices;
    private int newId = 0;
    private int serviceId = 1;
    private static ProductManager instance;

    private ProductManager() {
        this.catalogProducts = new ArrayList<>();
        this.catalogServices = new ArrayList<>();
    }

    public static ProductManager getInstance() {
        if (instance == null)
            instance = new ProductManager();
        return instance;
    }

    public boolean addProduct(Product product) {
        boolean added = false;
        if (catalogProducts.size() + catalogServices.size() < MAX_DIF_PRODUCTS) {
            if (!idExists(product.getId())) {
                catalogProducts.add(product);
                added = true;
            } else {
                CLI.print("Product or Event with id: " + product.getId() + " already exist.");
            }
        } else {
            CLI.print("Maximum products reached.");
        }
        return added;
    }

    public boolean addService(ProductService product) {
        boolean added = false;
        if (catalogProducts.size() + catalogServices.size() < MAX_DIF_PRODUCTS) {
            catalogServices.add(product);
            added = true;
        } else {
            CLI.print("Maximum products reached.");
        }
        return added;
    }

    public Item getIProduct(String id) {
        for (Product p : catalogProducts) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        for (ProductService s : catalogServices) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public List<Product> getCatalogProducts() {
        return catalogProducts;
    }

    public List<ProductService> getCatalogServices() {
        return catalogServices;
    }

    public boolean remove(String id) {
        int index = -1;
        if (id.endsWith("S")) {
            for (int i = 0; i < catalogServices.size(); i++) {
                if (catalogServices.get(i).getId().equals(id)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                catalogServices.remove(index);
                return true;
            }
        } else {
            for (int i = 0; i < catalogProducts.size(); i++) {
                if (catalogProducts.get(i).getId().equals(id)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                catalogProducts.remove(index);
                return true;
            }
        }
        return false;
    }

    public int indexOfIProduct(String id) {
        if (id.endsWith("S")) {
            for (int i = 0; i < catalogServices.size(); i++) {
                if (catalogServices.get(i).getId().equals(id)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < catalogProducts.size(); i++) {
                if (catalogProducts.get(i).getId().equals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @JsonIgnore
    public boolean idExists(String id) {
        return this.indexOfIProduct(id) != -1;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return catalogProducts.isEmpty();
    }

    public String generateNewProductId() {
        while (idExists(Integer.toString(newId)))
            newId++;
        return Integer.toString(newId);
    }

    public String generateNewServiceId() {
        while (idExists(serviceId +"S"))
            serviceId++;
        return serviceId+"S";
    }

}

