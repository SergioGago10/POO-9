package upm.Commands;

import upm.CLI;
import upm.Products.Catalog;
import upm.Products.Event;
import upm.Products.IProduct;
import upm.Products.TypeEvent;
import upm.Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProdAddFoodCommand extends Command {

    public ProdAddFoodCommand() {
        super("addFood");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        if (args.length < 6) {
            CLI.print("Format must be: " +
                    "prod addFood [<id>] \"< name>\" <price> <expiration: yyyy-MM-dd> <max_people>");
        } else {
            try {
                int i = 2;
                int id;
                IProduct product;
                if (args[i].contains("\"")) {
                    id = Catalog.generateNewProductId();
                } else {
                    id = Integer.parseInt(args[i]);
                    i++;
                }
                String name = args[i].replace("\"", "");
                i++;
                double price = Double.parseDouble(args[i]);
                i++;
                String[] dateStr = args[i].split("-");
                int expirationYear = Integer.parseInt(dateStr[0]);
                int expirationMonth = Integer.parseInt(dateStr[1]);
                int expirationDay = Integer.parseInt(dateStr[2]);
                LocalDate date = LocalDate.of(expirationYear, expirationMonth, expirationDay);
                i++;
                int maxPeople = Integer.parseInt(args[i]);
                if (maxPeople > 100) {
                    CLI.print("Error processing ->prod addFood ->Error adding product");
                    return false;
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
                        if (creationDate.plusDays(3).isAfter(date.atStartOfDay())) {
                            CLI.print("The meeting should be planned at least 3 days before");
                            return false;
                        }

                        product = new Event(id, name, price, creationDate, date, maxPeople, TypeEvent.FOOD);
                    } else {
                        if (LocalDateTime.now().plusDays(3).isAfter(date.atStartOfDay())) {
                            CLI.print("The meeting should be planned at least 3 days before");
                            return false;
                        } else
                            product = new Event(id, name, price, date, maxPeople, TypeEvent.FOOD);
                    }
                    if (Utilities.isValidProd(id, name, price)) {
                        Catalog.addProduct(product);
                        CLI.print(product.toString());
                        applied = true;
                        CLI.print("prod addFood: ok");
                    }
                }

            } catch (NumberFormatException ex) {
                CLI.print("Id and max personalization must be integer and price must be double," +
                        "date format: yyyy-MM-dd");
            }
        }
        return applied;
    }
}
