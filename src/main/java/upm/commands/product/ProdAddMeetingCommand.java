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

public class ProdAddMeetingCommand extends Command {
    public ProdAddMeetingCommand() {
        super("addMeeting");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 6) {
            CLI.printErrorNextLine("Error -> Format must be: " +
                    "prod addMeeting [<id>] \"<name>\" <price> < expiration: yyyy-MM-dd> <\n max_people >");
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
                    CLI.printErrorNextLine("Error -> Name must be between quotes (\" \")");
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
                String[] dateStr = args[i].split("-");
                int expirationYear = Integer.parseInt(dateStr[0]);
                int expirationMonth = Integer.parseInt(dateStr[1]);
                int expirationDay = Integer.parseInt(dateStr[2]);
                LocalDateTime date = LocalDate.of(expirationYear, expirationMonth, expirationDay).atStartOfDay();
                i++;
                try {
                    maxPeople = Integer.parseInt(args[i]);
                } catch (NumberFormatException exc) {
                    CLI.printErrorNextLine("Error -> Max people must be an integer number.");
                    return false;
                }
                if (maxPeople > 100) {
                    CLI.printErrorNextLine("Error -> Error processing ->prod addMeeting ->Error adding product");
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
                        if (creationDate.plusHours(12).isAfter(date)) {
                            CLI.printErrorNextLine("Error -> The meeting should be planned at least 12 hours before");
                            return true;
                        }
                        product = new Event(id, name, price, creationDate, date, maxPeople, TypeEvent.MEETING);
                    } else {
                        if (LocalDateTime.now().plusHours(12).isAfter(date)) {
                            CLI.printErrorNextLine("Error -> The meeting should be planned at least 12 hours before");
                            return true;
                        }
                        product = new Event(id, name, price, date, maxPeople, TypeEvent.MEETING);
                    }
                    if (productManager.addProduct(product)) {
                        CLI.printNextLine(product.toString());
                        CLI.printNextLine("prod addMeeting: ok");
                    }
                }

            } catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException ex) {
                CLI.printErrorNextLine("Error -> Expiration date must have the next format: yyyy-mm-dd");
                return true;
            }
        }
        return true;
    }
}
