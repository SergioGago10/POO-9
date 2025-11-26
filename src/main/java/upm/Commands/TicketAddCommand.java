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
        //todo hacer que el comando sea menos denso
        boolean applied = false;
        if (args.length < 6) {
            System.err.println("ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]");
        } else {
            try {
                String ticketId = args[2];
                int cashId = Integer.parseInt(args[3]);
                int prodId = Integer.parseInt(args[4]);
                int amount = Integer.parseInt(args[5]);
                if (Catalog.idExists(prodId)) {
                    Ticket ticketAModificar = TicketManager.getTicketById(ticketId);
                    if(ticketAModificar != null){
                        if(ticketAModificar.getCashId() != cashId){
                            System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                        } else{
                            if(args.length == 6){ //producto sin personalizaciones
                                ticketAModificar.addProductToTicket(prodId,amount,null);
                                applied = true;
                            } else{
                                ArrayList<String> customTexts = new ArrayList<>();
                                boolean correctFormat = true;
                                for (int i = 6; (i < args.length) && (correctFormat); i++) {
                                    String s = args[i];
                                    if (!s.startsWith("--p")) {
                                        System.err.println("Error: [--p<txt>] is the correct usage, try again." + s);
                                        correctFormat = false;
                                    }
                                    if (correctFormat){
                                        customTexts.add(s.substring(3));
                                    }
                                }
                                if(correctFormat){
                                    ticketAModificar.addProductToTicket(prodId, amount, customTexts);
                                    applied = true;
                                }
                            }
                        }
                    } else {
                        System.err.println("Error: Ticket "  + ticketId + " does not exist.");
                    }
                } else {
                    System.err.println("prodId must be an id contained in the catalog. Type 'prod list' to see all the catalog.");
                }
            } catch (NumberFormatException e) {
                System.err.println("prodId and cantidad must be integers.");
            } catch (Exception e) {
                System.err.println("Error adding product to ticket: " + e.getMessage());
            }
        }
        return applied;
    }
}
