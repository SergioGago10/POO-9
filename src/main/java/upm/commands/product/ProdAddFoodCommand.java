package upm.commands.product;

import upm.CLI;
import upm.Utilities;
import upm.commands.core.Command;
import upm.products.Event;
import upm.products.Product;
import upm.products.ProductManager;
import upm.products.TypeEvent;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProdAddFoodCommand extends Command {

    public ProdAddFoodCommand() {
        super("addFood");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 6) {
            CLI.print("Format must be: " +
                    "prod addFood [<id>] \"< name>\" <price> <expiration: yyyy-MM-dd> <max_people>");
        } else {
            try {
                int i = 2;
                String id;
                String name;
                double price;
                Product product;
                int maxPeople;
                ProductManager productManager = ProductManager.getInstance();
                if (args[i].contains("\"")) {
                    id = productManager.generateNewProductId();
                } else {
                    id = args[i];
                    i++;
                }
                if (args[i].contains("\""))
                    name = "'" + args[i].trim().replaceAll("^([\"'])|([\"'])$", "") + "'";
                else {
                    CLI.print("Name must be between quotes (\" \")");
                    return false;
                }
                i++;
                try {
                    price = Double.parseDouble(args[i]);
                } catch (NumberFormatException ex) {
                    CLI.print("Price must be double");
                    return false;
                }
                i++;
                String[] dateStr = args[i].split("-");
                int expirationYear = Integer.parseInt(dateStr[0]);
                int expirationMonth = Integer.parseInt(dateStr[1]);
                int expirationDay = Integer.parseInt(dateStr[2]);
                LocalDateTime date = LocalDate.of(expirationYear, expirationMonth, expirationDay).atStartOfDay();
                i++;
                try {
                    maxPeople = Integer.parseInt(args[i]);
                } catch (NumberFormatException exc) {
                    CLI.print("Max people must be an integer number.");
                    return false;
                }
                if (maxPeople > 100) {
                    CLI.print("Error processing ->prod addFood ->Error adding product");
                    return true;
                }
                i++;
                if (Utilities.isValidProd(id, name, price)) {
                    if (i == args.length - 1) {
                        String[] creationDateStr = args[i].split("-");
                        int creationYear = Integer.parseInt(creationDateStr[0]);
                        int creationMonth = Integer.parseInt(creationDateStr[1]);
                        int creationDay = Integer.parseInt(creationDateStr[2]);
                        int creationHour = Integer.parseInt(creationDateStr[3]);
                        int creationMinute = Integer.parseInt(creationDateStr[4]);
                        LocalDateTime creationDate = LocalDateTime.of(creationYear, creationMonth, creationDay,
                                creationHour, creationMinute);
                        if (creationDate.plusDays(3).isAfter(date)) {
                            CLI.print("The food should be planned at least 3 days before");
                            return true;
                        }

                        product = new Event(id, name, price, creationDate, date, maxPeople, TypeEvent.FOOD);
                    } else {
                        if (LocalDateTime.now().plusDays(3).isAfter(date)) {
                            CLI.print("The food should be planned at least 3 days before");
                            return true;
                        } else
                            product = new Event(id, name, price, date, maxPeople, TypeEvent.FOOD);
                    }
                    if (productManager.addProduct(product)) {
                        CLI.print(product.toString());
                        CLI.print("prod addFood: ok");
                    }
                }

            } catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException ex) {
                CLI.print("Expiration date must have the next format: yyyy-mm-dd");
            }
        }
        return true;
    }
}
