package upm.Commands;

import upm.Users.Cash;
import upm.Users.CashManager;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;

import java.util.Comparator;
import java.util.List;

public class CashTicketCommand extends Command {

    public CashTicketCommand() {
        super("tickets");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 3) {
            return false;
        }

        try {
            String cashId = args[2];

            Cash cash = CashManager.getCashById(cashId);
            if (cash == null) {
                System.out.println("Cash not found.");
                return false;
            }

            List<Ticket> tickets = TicketManager.printTicketsByCashier(cashId);

            System.out.println("Tickets: ");
            if (tickets != null && !tickets.isEmpty()) {
                tickets.sort(Comparator.comparing(Ticket::getTicketId));
                for (Ticket ticket : tickets) {
                    System.out.println("  " + ticket.getTicketId() + "->" + ticket.getEstado().name());
                }
            }

            System.out.println("cash tickets: ok");
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}