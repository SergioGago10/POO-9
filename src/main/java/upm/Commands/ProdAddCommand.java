package upm.Commands;

import upm.CLI;
import upm.Products.*;
import upm.Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

public class ProdAddCommand extends Command {

    public ProdAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 4) {
            CLI.print("Format must be: prod add ([<id>] \"<name>\" <category> <price> [<maxPers>]) || " +
                    "(\"<expiration:yyyy-MM-dd>\" <category> )");
        } else {
            try {
                String id;
                ServiceCategory serviceCategory;
                ProductService service;
                ProductManager productManager = ProductManager.getInstance();
                if (args[2].contains("-")) {
                    id = productManager.generateNewServiceId();
                    String[] dateStr = args[2].split("-");
                    int expirationYear = Integer.parseInt(dateStr[0]);
                    int expirationMonth = Integer.parseInt(dateStr[1]);
                    int expirationDay = Integer.parseInt(dateStr[2]);
                    LocalDateTime date = LocalDate.of(expirationYear, expirationMonth, expirationDay).atStartOfDay();
                    serviceCategory = ServiceCategory.valueOf(args[3]);
                    if (LocalDateTime.now().isBefore(date)) {
                        service = new ProductService(id, serviceCategory, date);
                        productManager.addService(service);
                        CLI.print(service.toString());
                        CLI.print("prod add:ok");
                        return true;
                    } else{
                        CLI.print("The service must have a date that has not passed.");
                    }
                } else {
                    int i = 2;
                    String name;
                    Category category;
                    double price;
                    Product product;
                    if (args[i].contains("\"")) {
                        id = productManager.generateNewProductId();
                    } else {
                        id = args[i];
                        i++;
                    }
                    name = "'" + args[i].trim().replaceAll("^([\"'])|([\"'])$", "") + "'";
                    if (name.length() > ProductManager.MAX_CHAR_NAME) {
                        CLI.print("name length must be lower than" + ProductManager.MAX_CHAR_NAME);
                        return true;
                    }
                    i++;
                    category = Category.valueOf(args[i]);
                    i++;
                    price = Double.parseDouble(args[i]);
                    if (price < 0) {
                        CLI.print("Price must be positive");
                        return true;
                    }
                    i++;
                    if (Utilities.isValidProd(Integer.parseInt(id), name, price)) {
                        if (i == args.length - 1) {
                            int maxPers = Integer.parseInt(args[i]);
                            product = new CustomizableProduct(id, name, category, price, maxPers);
                        } else
                            product = new BasicProduct(id, name, category, price);
                        if (productManager.addProduct(product)) {
                            productManager.getCatalogProducts().sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
                            CLI.print(product.toString());
                            CLI.print("prod add:ok");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                CLI.print("Max personalization must be integer and price must be double");
            } catch (IllegalArgumentException exc) {
                CLI.print("Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC");
            }
        }
        return true;
    }
}
