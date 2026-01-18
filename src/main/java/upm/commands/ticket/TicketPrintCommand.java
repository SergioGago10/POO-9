package upm.commands.ticket;

import upm.CLI;
import upm.commands.core.Command;
import upm.tickets.format.TicketFormatter;
import upm.users.Cash;
import upm.users.UserManager;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;
import upm.tickets.core.TicketState;

import java.util.List;
import java.util.Objects;

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

            TicketManager ticketManager = TicketManager.getInstance();
            Ticket<?> ticketAMostrar = ticketManager.getTicketById(ticketId);

            if(ticketAMostrar == null) {
                CLI.printErrorNextLine("Error -> Ticket with id: " + ticketId + " does not exist.");
                return true;
            }

            Cash cashUser = (Cash) UserManager.getInstance().getUserByID(cashId);
            if (cashUser == null) {
                CLI.printErrorNextLine("Error -> Cashier with id: " + cashId + " does not exist.");
                return true;
            }

            if (!containsTicketId(cashUser.getTickets(), ticketAMostrar)) {
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

    private boolean containsTicketId(List<?> tickets, Ticket<?> target) {
        String targetId = safeTicketId(target);
        if (targetId == null) return false;
        if (tickets == null) return false;

        for (Object o : tickets) {
            if (o instanceof Ticket<?> t) {
                if (Objects.equals(safeTicketId(t), targetId)) return true;
            }
        }
        return false;
    }

    private String safeTicketId(Ticket<?> t) {
        if (t == null) return null;
        if (t.getTicketMetadata() == null) return null;
        return t.getTicketMetadata().getTicketID();
    }
}
