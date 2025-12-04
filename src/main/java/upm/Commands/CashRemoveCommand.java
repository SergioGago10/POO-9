package upm.Commands;

import upm.CLI;
import upm.Users.Cash;
import upm.Users.UserManager;

public class CashRemoveCommand extends Command {
    public CashRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied=false;
        UserManager userManager=UserManager.getInstance();
        if (args.length < 3) {
            System.out.println("Format must be: cash remove <cashierId>");
        } else {
            String identifier = args[2];
            try {
                Cash cash = (Cash) userManager.getUserByID(identifier);
                if (cash == null) {
                    CLI.print("Cashier not found.");
                } else if (UserManager.removeUserByDni(identifier)) {
                    CLI.print(cash.toString());
                    CLI.print("cash remove: ok");
                    applied = true;
                } else {
                    CLI.print("Cashier couldn't be removed.");
                }
            }catch (ClassCastException ex){
                CLI.print("Id doesnt belong to a cahier, it belongs to a Client.");
            }
        }
        return applied;
    }
}
