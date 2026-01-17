package upm.commands.cash;

import upm.CLI;
import upm.commands.core.Command;
import upm.tickets.format.TicketFormatter;
import upm.users.Cash;
import upm.users.UserManager;

public class CashTicketCommand extends Command {

    public CashTicketCommand() {
        super("tickets");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 3) {
            CLI.printErrorNextLine("Error -> Format must be: cash list");
            return false;
        }

        try {
            String cashIdentifier = args[2];
            UserManager userManager=UserManager.getInstance();
            Cash cash = (Cash) userManager.getUserByID(cashIdentifier);

            if (cash == null) {
                CLI.printErrorNextLine("Error -> Cash not found.");
                return true;
            }

            TicketFormatter ticketFormatter = new TicketFormatter();
            ticketFormatter.printTicketsByCash(cashIdentifier);
            CLI.printNextLine("cash tickets: ok");
            return true;
        } catch (ClassCastException ex) {
            CLI.printErrorNextLine("Error -> Id doesnt belong to a cashier, it belongs to a Client.");
            return true;
        }
    }
}