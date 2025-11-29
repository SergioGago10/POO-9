package upm.Commands;

import upm.CLI;
import upm.Users.Cash;
import upm.Users.CashManager;

public class CashRemoveCommand extends Command {
    public CashRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied;

        if (args.length < 3) {
            System.out.println("Format must be: cash remove <cashierId>");
            applied = false;
        } else {
            String identifier = args[2];
            Cash cash = CashManager.getCashByIdentifier(identifier);

            if (cash == null) {
                CLI.print("Cashier not found.");
                applied = false;
            } else if (CashManager.removeCashByIdentifier(identifier)) {
                CLI.print(cash.toString());
                CLI.print("cash remove: ok");
                applied = true;
            } else {
                CLI.print("Cashier couldn't be removed.");
                applied = false;
            }
        }

        return applied;
    }
}
