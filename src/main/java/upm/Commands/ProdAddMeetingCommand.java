package upm.Commands;

import upm.CLI;
import upm.Products.Catalog;
import upm.Products.Event;
import upm.Products.IProduct;
import upm.Products.TypeEvent;
import upm.Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProdAddMeetingCommand extends Command {
    public ProdAddMeetingCommand() {
        super("addMeeting");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 6) {
            CLI.print("Format must be: " +
                    "prod addMeeting [<id>] \"<name>\" <price> < expiration: yyyy-MM-dd> <\n max_people >");
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
                LocalDateTime date = LocalDate.of(expirationYear, expirationMonth, expirationDay).atStartOfDay();
                i++;
                int maxPeople = Integer.parseInt(args[i]);
                if (maxPeople > 100) {
                    CLI.print("Error processing ->prod addMeeting ->Error adding product");
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
                            CLI.print("The meeting should be planned at least 12 hours before");
                            return true;
                        }
                        product = new Event(id, name, price, creationDate, date, maxPeople, TypeEvent.MEETING);
                    } else {
                        if (LocalDateTime.now().plusHours(12).isAfter(date)) {
                            CLI.print("The meeting should be planned at least 12 hours before");
                            return true;
                        }
                        product = new Event(id, name, price, date, maxPeople, TypeEvent.MEETING);
                    }
                    if (Catalog.addProduct(product)) {
                        Catalog.addProduct(product);
                        CLI.print(product.toString());
                        CLI.print("prod addMeeting: ok");
                    }
                }

            } catch (NumberFormatException ex) {
                CLI.print("NumberFormat Error" + ex);
                return true;
            }
        }
        return true;
    }
}
