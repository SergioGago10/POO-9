package upm.commands.ticket;

import upm.CLI;
import upm.commands.core.Command;
import upm.tickets.format.TicketFormatter;
import upm.users.Cash;
import upm.users.UserManager;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;
import upm.tickets.core.TicketState;

public class TicketPrintCommand extends Command {

    public TicketPrintCommand() {
        super("print");
    }

    @Override
    public boolean apply(String[] args) {
        if(args.length != 4) {
            CLI.printErrorNextLine("Error -> format must be: ticket print <ticketId> <cashId>");
            return true;
        }
        try {
            String ticketId = args[2];
            String cashId = args[3];
            TicketManager ticketManager=TicketManager.getInstance();
            Ticket<?> ticketAMostrar = ticketManager.getTicketById(ticketId);

            if(ticketAMostrar == null) {
                CLI.printErrorNextLine("Error -> Ticket with id: " + ticketId + " does not exist.");
                return true;
            }

            Cash cashUser = (Cash) UserManager.getInstance().getUserByID(cashId);

            if(!cashUser.getTickets().contains(ticketAMostrar)) {
                CLI.printErrorNextLine("Error -> Ticket " + ticketId + " does not belong to cashier " + cashId);
                return true;
            }

            if(ticketAMostrar.getEstado() != TicketState.EMPTY){
                ticketAMostrar.closeTicket();
            }

            TicketFormatter ticketFormatter = new TicketFormatter();
            ticketFormatter.printFinalTicket(ticketAMostrar);
            CLI.printNextLine("ticket print: ok");
        } catch (Exception e) {
            CLI.printErrorNextLine("Error -> Ticket could not be printed: " + e.getMessage());
        }
        return true;
    }

}
