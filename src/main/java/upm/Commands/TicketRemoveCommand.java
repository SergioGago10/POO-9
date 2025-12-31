package upm.Commands;

import upm.Products.ProductManager;
import upm.Users.Cash;
import upm.Users.UserManager;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;

public class TicketRemoveCommand  extends Command{

    public TicketRemoveCommand(){
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length != 5) {
            System.err.println("Usage: ticket remove <ticketId> <cashId> <prodId>");
        } else {
            try {
                String ticketId = args[2];
                String cashId = args[3];
                ProductManager productManager=ProductManager.getInstance();
                TicketManager ticketManager=TicketManager.getInstance();
                String prodId = args[4];
                if (productManager.idExists(prodId)) {
                    Ticket ticketAModificar = ticketManager.getTicketById(ticketId); //Si es null es que no existe dicho ticketId!
                    if(ticketAModificar == null){
                        System.err.println("Error: Ticket " + ticketId + " does not exist.");
                    } else {
                        Cash cashUser = (Cash) UserManager.getInstance().getUserByID(cashId);
                        if(!cashUser.getTickets().contains(ticketAModificar)){
                            System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                        }else {
                            ticketAModificar.removeProductFromTicket(prodId);
                            ticketManager.getFormatter().printCurrentTicket(ticketAModificar);
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
        return true;
    }

}
