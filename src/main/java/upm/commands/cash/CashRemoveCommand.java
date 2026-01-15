package upm.commands.cash;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Cash;
import upm.users.UserManager;

public class CashRemoveCommand extends Command {
    public CashRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        UserManager userManager = UserManager.getInstance();
        if (args.length < 3) {
            CLI.printErrorNextLine("Error -> Format must be: cash remove <cashierId>");
        } else {
            String identifier = args[2];
            try {
                Cash cash = (Cash) userManager.getUserByID(identifier);
                if (cash == null) {
                    CLI.printErrorNextLine("Error -> Cashier not found.");
                } else {
                    if (userManager.removeUserByDni(identifier)) {
                        CLI.printNextLine(cash.toString());
                        CLI.printNextLine("cash remove: ok");
                        applied = true;
                    } else {
                        CLI.printErrorNextLine("Error -> Cashier couldn't be removed.");
                    }
                }
            } catch (ClassCastException ex) {
                CLI.printErrorNextLine("Error -> Id doesnt belong to a cahier, it belongs to a Client.");
            }
        }
        return applied;
    }
}
