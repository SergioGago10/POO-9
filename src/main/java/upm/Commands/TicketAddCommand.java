package upm.Commands;

import upm.Products.ProductManager;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;

import java.util.ArrayList;

public class TicketAddCommand extends Command {

    public TicketAddCommand(){
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 6) {
            System.err.println("ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]");
            return true;
        }

        try {
            String ticketId = args[2];
            String cashId = args[3];
            int prodId = Integer.parseInt(args[4]);
            int amount = Integer.parseInt(args[5]);

            // Validación del producto
            if (!ProductManager.idExists(prodId)) {
                System.err.println("prodId must be an id contained in the catalog. Type 'prod list' to see all the catalog.");
                return true;
            }

            // Buscar ticket
            Ticket ticketAModificar = TicketManager.getTicketById(ticketId);

            if (ticketAModificar == null) {
                System.err.println("Error: Ticket " + ticketId + " does not exist.");
                return true;
            }

            // Comprobar que pertenece al mismo cashId
            if (!ticketAModificar.getCashId().equals(cashId)) {
                System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                return true;
            }

            // Customizaciones
            ArrayList<String> customTexts = parseCustomizations(args);
            ticketAModificar.addProductToTicket(prodId, amount, customTexts);

        } catch (NumberFormatException e) {
            System.err.println("amount must be an integer.");
        } catch (Exception e) {
            System.err.println("Error adding product to ticket: " + e.getMessage());
        }

        return true;
    }

    private ArrayList<String> parseCustomizations(String[] args) {
        if (args.length <= 6) return null;

        ArrayList<String> customizations = new ArrayList<>();
        boolean correctFormat = true;

        for (int i = 6; i < args.length && correctFormat; i++) {
            String s = args[i];
            if (!s.startsWith("--p")) {
                System.err.println("Error: expected --p<txt>, found: " + s);
                correctFormat = false;
            }
            if (correctFormat) {
                customizations.add(s.substring(3));
            }
        }

        return correctFormat ? customizations : null;
    }
}
