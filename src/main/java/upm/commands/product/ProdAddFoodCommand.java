package upm.commands.product;

import upm.CLI;
import upm.commands.core.Command;
import upm.products.ProductManager;
import upm.products.Event;
import upm.products.Product;
import upm.products.TypeEvent;
import upm.Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProdAddFoodCommand extends Command {

    public ProdAddFoodCommand() {
        super("addFood");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 6) {
            CLI.printErrorNextLine("Error -> Format must be: " +
                    "prod addFood [<id>] \"< name>\" <price> <expiration: yyyy-MM-dd> <max_people>");
            return true;
        }
        try {
            int i = 2;
            String id;
            Product product;
            ProductManager productManager=ProductManager.getInstance();

            //[i++] elige el valor de i, y luego lo incrementa
            id = args[i].contains("\"") ?  productManager.generateNewProductId() : args[i++];

            String name = "'" + args[i++].trim().replaceAll("^([\"'])|([\"'])$", "") + "'";
            double price = Double.parseDouble(args[i++]);

            String[] dateStr = args[i++].split("-");
            int expirationYear = Integer.parseInt(dateStr[0]);
            int expirationMonth = Integer.parseInt(dateStr[1]);
            int expirationDay = Integer.parseInt(dateStr[2]);
            LocalDateTime date = LocalDate.of(expirationYear, expirationMonth, expirationDay).atStartOfDay();

            int maxPeople = Integer.parseInt(args[i++]);
            if (maxPeople > 100) {
                CLI.printErrorNextLine("Error processing ->prod addFood ->Error adding product");
                return true;
            }

            if(!Utilities.isValidProd(id, name, price)){
                //Los mensajes de error ya se gestionan en isValidProd, por lo que no será necesario poner nuevos aquí
                return true;
            }

            if (i == args.length - 1) {
                LocalDateTime creationDate = getLocalDateTime(args[i]);
                if (creationDate.plusDays(3).isAfter(date)) {
                    CLI.printErrorNextLine("Error -> The meeting should be planned at least 3 days before");
                    return true;
                }

                product = new Event(id, name, price, creationDate, date, maxPeople, TypeEvent.FOOD);
            } else {
                if (LocalDateTime.now().plusDays(3).isAfter(date)) {
                    CLI.printErrorNextLine("Error -> The meeting should be planned at least 3 days before");
                    return true;
                }

                product = new Event(id, name, price, date, maxPeople, TypeEvent.FOOD);
            }

            if (productManager.addProduct(product)) {
                CLI.printNextLine(product.toString());
                CLI.printNextLine("prod addFood: ok");
            }

        } catch (NumberFormatException ex) {
            CLI.printErrorNextLine("Error -> Id and max personalization must be integer and price must be double," +
                    "date format: yyyy-MM-dd");
        }
        return true;
    }

    private static LocalDateTime getLocalDateTime(String args) {
        String[] creationDateStr = args.split("-");
        int creationYear = Integer.parseInt(creationDateStr[0]);
        int creationMonth = Integer.parseInt(creationDateStr[1]);
        int creationDay = Integer.parseInt(creationDateStr[2]);
        int creationHour = Integer.parseInt(creationDateStr[3]);
        int creationMinute = Integer.parseInt(creationDateStr[4]);
        return LocalDateTime.of(creationYear, creationMonth, creationDay,
                creationHour, creationMinute);
    }
}
