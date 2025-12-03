package upm.Commands;

import upm.CLI;
import upm.Users.Cash;
import upm.Users.CashManager;
import upm.Users.Client;
import upm.Users.ClientsManager;
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
            return true;
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

        if (!CashManager.idExists(cashId)) {
            CLI.print("Cashier ID does not exist: " + cashId);
            return true;
        }

        if (!ClientsManager.dniExists(userDni)) {
            CLI.print("Client DNI does not exist: " + userDni);
            return true;
        }

        if (ticketId == null) {
            ticket=TicketManager.newTicket(cashId, userDni);
        } else {
            ticket=TicketManager.newTicket(ticketId, cashId, userDni, false);
        }
        Cash cashier=CashManager.getCashByIdentifier(cashId);
        Client client=ClientsManager.getClientByDni(userDni);
        cashier.addTicket(ticket);
        client.addTicket(ticket);
        CLI.print("ticket new: ok");
        return true;
    }
}
