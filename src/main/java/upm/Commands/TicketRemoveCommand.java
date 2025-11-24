package upm.Commands;

import upm.Products.Catalog;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;

public class TicketRemoveCommand  extends  TicketCommand{

    public TicketRemoveCommand(TicketManager ticketManager){
        super("remove",ticketManager);
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        if (args.length != 5) {
            System.err.println("Usage: ticket remove <ticketId> <cashId> <prodId>");
        } else {
            try {
                String ticketId = args[2];
                int cashId = Integer.parseInt(args[3]);
                int prodId = Integer.parseInt(args[4]);
                if (Catalog.idExists(prodId)) {
                    Ticket ticketAModificar = ticketManager.getTicketById(ticketId); //Si es null es que no existe dicho ticketId!
                    if(ticketAModificar == null){
                        System.err.println("Error: Ticket " + ticketId + " does not exist.");
                    } else {
                        if(ticketAModificar.getCashId() != cashId){
                            System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                        }else {
                            ticketAModificar.removeProductFromTicket(prodId);
                            System.out.println("ticket remove: ok");
                        }
                    }
                } else
                    System.out.println("Product with the id " + prodId + " was not found.");
            } catch (NumberFormatException e) {
                System.err.println("prodId must be an integer.");
            } catch (Exception e) {
                System.err.println("Error removing product from ticket: " + e.getMessage());
            }
        }
        return applied;
    }

}
