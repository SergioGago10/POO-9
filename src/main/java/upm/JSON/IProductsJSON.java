package upm.JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import upm.CLI;
import upm.Products.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class IProductsJSON {
    public static void main(String[] args) {
        ProductManager productManager = ProductManager.getInstance();
        Product product = new BasicProduct("1", "Troteras y danceras", Category.BOOK, 12);
        productManager.addProduct(product);
        ProductService service = new ProductService("1", ServiceCategory.TRANSPORT, LocalDateTime.now());
        ProductService service2 = new ProductService("2", ServiceCategory.INSURANCE, LocalDateTime.now());
        productManager.addService(service);
        productManager.addService(service2);
        crearJSONIProduct();
        leerJSON();
        for (Product products : productManager.getCatalogProducts()) {
            CLI.print(products.toString());
        }
        for (ProductService services : productManager.getCatalogServices()) {
            CLI.print(services.toString());
        }
    }

    public static void crearJSONIProduct() {
        ProductManager productManager = ProductManager.getInstance();
        List<ProductService> servicesList = productManager.getCatalogServices();
        File fileJSON = new File("Products.ProductManager.json");
        if (fileJSON.exists())
            fileJSON.delete();
        try (FileWriter file = new FileWriter(fileJSON)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("Products.ProductManager.json"), productManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void leerJSON() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ProductManager pm = ProductManager.getInstance();

        try {
            JsonNode root = objectMapper.readTree(new File("Products.ProductManager.json"));

            JsonNode productsNode = root.get("catalogProducts");
            if (productsNode != null && productsNode.isArray()) {
                List<Product> products =
                        objectMapper.convertValue(
                                productsNode,
                                new TypeReference<List<Product>>() {
                                }
                        );
                pm.getCatalogProducts().clear();
                pm.getCatalogProducts().addAll(products);
            }

            JsonNode servicesNode = root.get("catalogServices");
            if (servicesNode != null && servicesNode.isArray()) {
                List<ProductService> services =
                        objectMapper.convertValue(
                                servicesNode,
                                new TypeReference<List<ProductService>>() {
                                }
                        );
                pm.getCatalogServices().clear();
                pm.getCatalogServices().addAll(services);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
