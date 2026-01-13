package upm.commands.cash;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Cash;
import upm.users.UserManager;
import upm.tickets.management.TicketManager;

public class CashTicketCommand extends Command {

    public CashTicketCommand() {
        super("tickets");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 3) {
            CLI.print("Format of instruction: cash list");
            return false;
        }

        try {
            String cashIdentifier = args[2];
            UserManager userManager=UserManager.getInstance();
            Cash cash = (Cash) userManager.getUserByID(cashIdentifier);
            if (cash == null) {
                CLI.print("Cash not found.");
                return false;
            }
            TicketManager ticketManager=TicketManager.getInstance();
            ticketManager.getFormatter().printTicketsByCash(cashIdentifier);
            CLI.print("cash tickets: ok");
            return true;

        } catch (ClassCastException ex) {
            CLI.print("Id doesnt belong to a cashier, it belongs to a Client.");
            return false;
        }
    }
}