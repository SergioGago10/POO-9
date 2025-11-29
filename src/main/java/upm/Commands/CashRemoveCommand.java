package upm.Commands;

import upm.CLI;
import upm.Users.Cash;
import upm.Users.CashManager;

public class CashRemoveCommand extends Command {
    public CashRemoveCommand(){
        super("remove");
    }


    @Override
    public boolean apply(String[] args) {
        boolean applied = false;

        if (args.length == 3) {
            String identifier = args[2];
            Cash cashRemoved = CashManager.getCashByIdentifier(identifier);
            if (cashRemoved != null) {
                System.out.println(cashRemoved.toString());

                if (CashManager.removeCashByIdentifier(identifier)) {
                    CLI.print("cash remove: ok");
                    applied = true;
                }

            } else {
                CLI.print("The cashier with id: " + identifier + " couldn't be removed. Cashier not found.");
            }
        }

        return applied;
    }
}


