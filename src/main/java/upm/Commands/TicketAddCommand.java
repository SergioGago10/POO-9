package upm.Commands;

import upm.Products.Catalog;
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
            return false;
        }
        try {
            String ticketId = args[2];
            int cashId = Integer.parseInt(args[3].substring(2));
            int prodId = Integer.parseInt(args[4]);
            int amount = Integer.parseInt(args[5]);

            if (!Catalog.idExists(prodId)) {
                System.err.println("prodId must be an id contained in the catalog. Type 'prod list' to see all the catalog.");
                return false;
            }

            Ticket ticketAModificar = TicketManager.getTicketById(ticketId);

            if(ticketAModificar == null){
                System.err.println("Error: Ticket "  + ticketId + " does not exist.");
                return false;
            }

            if(ticketAModificar.getCashId() != cashId){
                System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                return false;
            }

            ArrayList<String> customTexts = parseCustomizations(args);
            ticketAModificar.addProductToTicket(prodId,amount, customTexts);

        } catch (NumberFormatException e) {
            System.err.println("prodId and cantidad must be integers.");
        } catch (Exception e) {
            System.err.println("Error adding product to ticket: " + e.getMessage());
        }
        return true;
    }

    private ArrayList<String> parseCustomizations(String[] args){
        if(args.length<=6) return null; //Caso base, no hay productos customizados, devolvemos null.

        ArrayList<String> customizations = new ArrayList<>();
        boolean correctFormat = true;
        for (int i = 6; i < args.length && correctFormat; i++) {
            String s = args[i];
            if (!s.startsWith("--p")) {
                System.err.println("Error: expected --p<txt>, found: " + s);
                correctFormat = false;
            }
            if(correctFormat){
                customizations.add(s.substring(3));
            }
        }
        return correctFormat? customizations : null;
    }
}
