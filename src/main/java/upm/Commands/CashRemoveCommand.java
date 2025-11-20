package upm.Commands;

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
            String id = args[2];
            Cash cashRemoved = CashManager.getCashById(id);
            if (cashRemoved != null) {
                System.out.println(cashRemoved.toString());

                if (CashManager.removeCashById(id)) {
                    System.out.println("cash remove: ok");
                    applied = true;
                }

            } else {
                System.err.println("The cashier with id: " + id + " couldn't be removed. Cashier not found.");
            }
        }

        return applied;
    }
}


