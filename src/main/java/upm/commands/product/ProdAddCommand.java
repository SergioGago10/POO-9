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
            return true;
        }
        try {
            String id;
            ServiceCategory serviceCategory;
            ProductService service;
            ProductManager productManager = ProductManager.getInstance();

            if (args.length == 4) {
                id = productManager.generateNewServiceId();
                String[] dateStr = args[2].split("-");
                int expirationYear = Integer.parseInt(dateStr[0]);
                int expirationMonth = Integer.parseInt(dateStr[1]);
                int expirationDay = Integer.parseInt(dateStr[2]);
                LocalDateTime date = LocalDate.of(expirationYear, expirationMonth, expirationDay).atStartOfDay();
                serviceCategory = ServiceCategory.valueOf(args[3]);

                if (date.isBefore(LocalDateTime.now())) {
                    CLI.printErrorNextLine("Error -> The service must have a date that has not passed.");
                    return true;
                }

                service = new ProductService(id, serviceCategory, date);
                productManager.addService(service);
                CLI.printNextLine(service.toString());
                CLI.printNextLine("prod add: ok");
                return true;
            }

            int i = 2;
            String name;
            Category category;
            double price;
            Product product;

            //[i++] elige el valor de i, y luego lo incrementa
            id = args[i].contains("\"")? productManager.generateNewProductId() : args[i++];

            name = "'" + args[i++].trim().replaceAll("^([\"'])|([\"'])$", "") + "'";
            if (name.length() > ProductManager.MAX_CHAR_NAME) {
                CLI.printErrorNextLine("Error -> name length must be lower than" + ProductManager.MAX_CHAR_NAME);
                return true;
            }

            category = Category.valueOf(args[i++]);
            price = Double.parseDouble(args[i++]);

            if(!Utilities.isValidProd(id, name, price)){
                //Los mensajes de error ya se gestionan en isValidProd, por lo que no será necesario poner nuevos aquí
                return true;
            }

            if (i == args.length - 1) {
                int maxPers = Integer.parseInt(args[i]);
                product = new CustomizableProduct(id, name, category, price, maxPers);
            } else {
                product = new BasicProduct(id, name, category, price);
            }

            if (productManager.addProduct(product)) {
                productManager.getCatalogProducts().sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
                CLI.printNextLine(product.toString());
                CLI.printNextLine("prod add: ok");
            }

        } catch (NumberFormatException ex) {
            CLI.printErrorNextLine("Error -> Max personalization must be integer and price must be double");
        } catch (IllegalArgumentException exc) {
            CLI.printErrorNextLine("Error -> Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC");
        }

        return true;
    }
}
