package upm.Commands;

import upm.CLI;
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
            String cashIdentifier = args[2];

            Cash cash = CashManager.getCashByIdentifier(cashIdentifier);
            if (cash == null) {
                CLI.print("Cash not found.");
                return false;
            }

            List<Ticket> tickets = TicketManager.printTicketsByCashier(cashIdentifier);

            CLI.print("Tickets: ");
            if (tickets != null && !tickets.isEmpty()) {
                tickets.sort(Comparator.comparing(Ticket::getTicketId));
            }

            CLI.print("cash tickets: ok");
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}