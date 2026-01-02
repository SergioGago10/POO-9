package upm.JSON;

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

public class ItemsJSON {
    public static void main(String[] args) {
        ProductManager productManager = ProductManager.getInstance();
        Product product = new BasicProduct("1", "Troteras y danceras", Category.BOOK, 12);
        productManager.addProduct(product);
        ProductService service = new ProductService("1", ServiceCategory.TRANSPORT, LocalDateTime.now());
        ProductService service2 = new ProductService("2", ServiceCategory.INSURANCE, LocalDateTime.now());
        productManager.addService(service);
        productManager.addService(service2);
        createJSON();
        readJSON();
        for (Product products : productManager.getCatalogProducts()) {
            CLI.print(products.toString());
        }
        for (ProductService services : productManager.getCatalogServices()) {
            CLI.print(services.toString());
        }
    }

    public static void createJSON() {
        ProductManager productManager = ProductManager.getInstance();
        File fileJSON = new File("Items.json");
        if (fileJSON.exists())
            fileJSON.delete();
        try (FileWriter file = new FileWriter(fileJSON)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("Items.json"), productManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            ProductManager loaded =
                    objectMapper.readValue(
                            new File("Items.json"),
                            ProductManager.class
                    );
            ProductManager pm = ProductManager.getInstance();
            pm.getCatalogProducts().clear();
            pm.getCatalogProducts().addAll(loaded.getCatalogProducts());

            pm.getCatalogServices().clear();
            pm.getCatalogServices().addAll(loaded.getCatalogServices());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
