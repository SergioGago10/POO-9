package upm.Commands;

import upm.CLI;
import upm.Users.*;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;

public class TicketNewCommand extends Command {

    public TicketNewCommand() {
        super("new");
    }

    @Override
    public boolean apply(String[] args) {

        if (args.length < 4 || args.length > 5) {
            System.out.println("Usage: ticket new [<id>] <cashId> <userDni>");
            return false;
        }

        String ticketId = null;
        String cashId;
        String userDni;
        Ticket ticket;

        if (args.length == 4) {
            cashId = args[2];
            userDni = args[3];
        } else {
            ticketId = args[2];
            cashId = args[3];
            userDni = args[4];
        }
        UserManager userManager=UserManager.getInstance();
        if (!userManager.idExists(cashId)) {
            CLI.print("Cashier ID does not exist: " + cashId);
            return false;
        }

        if (!userManager.idExists(userDni)) {
            CLI.print("Client DNI does not exist: " + userDni);
            return false;
        }
        try {
            Cash cashier = (Cash) userManager.getUserByID(cashId);
            Client client = (Client) userManager.getUserByID(userDni);
            if (ticketId == null) {
                ticket = TicketManager.newTicket(cashId, userDni);
            } else {
                ticket = TicketManager.newTicket(ticketId, cashId, userDni, false);
            }

            cashier.addTicket(ticket);
            client.addTicket(ticket);
            CLI.print("ticket new: ok");
            return true;
        }catch (ClassCastException ex){
            CLI.print("First id must be a cash id and second id must be a client DNI.");
            return false;
        }

    }
}
