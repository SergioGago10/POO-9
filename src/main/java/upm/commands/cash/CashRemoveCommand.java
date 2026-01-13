package upm.commands.cash;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Cash;
import upm.users.UserManager;
import upm.tickets.management.TicketManager;

public class CashRemoveCommand extends Command {
    public CashRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        UserManager userManager = UserManager.getInstance();
        TicketManager ticketManager = TicketManager.getInstance();
        if (args.length < 3) {
            System.out.println("Format must be: cash remove <cashierId>");
        } else {
            String identifier = args[2];
            try {
                Cash cash = (Cash) userManager.getUserByID(identifier);
                if (cash == null) {
                    CLI.print("Cashier not found.");
                } else {
                    if (userManager.removeUserByDni(identifier)) {
                        CLI.print(cash.toString());
                        CLI.print("cash remove: ok");
                        applied = true;
                    } else {
                        CLI.print("Cashier couldn't be removed.");
                    }
                }
            } catch (ClassCastException ex) {
                CLI.print("Id doesnt belong to a cahier, it belongs to a Client.");
            }
        }
        return applied;
    }
}
