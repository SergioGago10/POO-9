package upm.commands.ticket;

import upm.CLI;
import upm.commands.core.Command;
import upm.products.ProductManager;
import upm.tickets.core.TicketState;
import upm.tickets.format.TicketFormatter;
import upm.users.Cash;
import upm.users.UserManager;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;

public class TicketRemoveCommand  extends Command {

    public TicketRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length != 5) {
            CLI.printErrorNextLine("Error -> format must be: ticket remove <ticketId> <cashId> <prodId>");
            return true;
        }
        try {
            String ticketId = args[2];
            String cashId = args[3];
            ProductManager productManager=ProductManager.getInstance();
            TicketManager ticketManager=TicketManager.getInstance();
            String prodId = args[4];

            if(!productManager.idExists(prodId)) {
                CLI.printErrorNextLine("Error -> Product with id: " + prodId + " does not exist.");
                return true;
            }

            Ticket<?> ticketAModificar = ticketManager.getTicketById(ticketId);

            if(ticketAModificar == null) {
                CLI.printErrorNextLine("Error -> Ticket with id: " + ticketId + " does not exist.");
                return true;
            }

            Cash cashUser = (Cash) UserManager.getInstance().getUserByID(cashId);

            if(!cashUser.getTickets().contains(ticketAModificar)) {
                CLI.printErrorNextLine("Error -> Ticket: " + ticketId + " does not belong to cashier " + cashId);
                return true;
            }

            TicketFormatter ticketFormatter = new TicketFormatter();
            ticketAModificar.removeProductFromTicket(prodId);

            //Si el ticket pasa de tener productos a tener 0, será empty de nuevo
            if(ticketAModificar.getItemsList().isEmpty()) {
                ticketAModificar.setEstado(TicketState.EMPTY);
            }

            ticketFormatter.printCurrentTicket(ticketAModificar);
            CLI.printNextLine("ticket remove: ok");
            } catch (NumberFormatException e) {
                CLI.printErrorNextLine("Error -> prodId must be an integer.");
            } catch (Exception e) {
                CLI.printErrorNextLine("Error -> product could not be removed from the ticket: " + e.getMessage());
            }
        return true;
    }
}
