package upm.commands.product;

import upm.CLI;
import upm.Utilities;
import upm.commands.core.Command;
import upm.products.*;

import java.time.DateTimeException;
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
            CLI.printErrorNextLine("Error -> Format must be: prod add ([<id>] \"<name>\" <category> <price> [<maxPers>]) || " +
                    "(\"<expiration:yyyy-MM-dd>\" <category> )");
        } else {
            try {
                String id;
                ServiceCategory serviceCategory;
                ProductService service;
                ProductManager productManager = ProductManager.getInstance();
                if (args.length == 4) {
                    id = productManager.generateNewServiceId();
                    String[] dateStr = args[2].split("-");
                    LocalDateTime date;
                    try {
                        int expirationYear = Integer.parseInt(dateStr[0]);
                        int expirationMonth = Integer.parseInt(dateStr[1]);
                        int expirationDay = Integer.parseInt(dateStr[2]);
                        date = LocalDate.of(expirationYear, expirationMonth, expirationDay).atStartOfDay();
                    } catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException exc) {
                        CLI.printErrorNextLine("Error -> Expiration date must have the next format: yyyy-mm-dd");
                        return false;
                    }
                    serviceCategory = ServiceCategory.valueOf(args[3]);
                    if (LocalDateTime.now().isBefore(date)) {
                        service = new ProductService(id, serviceCategory, date);
                        productManager.addService(service);
                        CLI.print(service.toString());
                        CLI.print("prod add:ok");
                        return true;
                    } else {
                        CLI.printErrorNextLine("Error -> The service must have a date that has not passed.");
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
                    if (args[i].contains("\""))
                        name = "'" + args[i].trim().replaceAll("^([\"'])|([\"'])$", "") + "'";
                    else {
                        CLI.printErrorNextLine("Error -> Name must be between quotes (\" \")");
                        return false;
                    }
                    i++;
                    try {
                        category = Category.valueOf(args[i]);
                    } catch (IllegalArgumentException exc) {
                        CLI.printErrorNextLine("Error -> Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC");
                        return false;
                    }
                    i++;
                    try {
                        price = Double.parseDouble(args[i]);
                    } catch (NumberFormatException ex) {
                        CLI.printErrorNextLine("Error -> Price must be double");
                        return false;
                    }
                    i++;
                    if (Utilities.isValidProd(id, name, price)) {
                        if (i == args.length - 1) {
                            int maxPers = Integer.parseInt(args[i]);
                            product = new CustomizableProduct(id, name, category, price, maxPers);
                        } else
                            product = new BasicProduct(id, name, category, price);
                        if (productManager.addProduct(product)) {
                            productManager.getCatalogProducts().sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
                            CLI.printNextLine(product.toString());
                            CLI.printNextLine("prod add:ok");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                CLI.printErrorNextLine("Error -> Max personalization must be integer");
            }
        }
        return true;
    }
}
