package upm.Commands;
import upm.CLI;
import upm.Users.Cash;
import upm.Users.CashManager;
import upm.Users.Client;
import upm.Users.ClientsManager;

import java.util.Collections;
import java.util.Comparator;

public class CashListCommand extends Command {
    public CashListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = true;
        if (CashManager.getCashList().size()==0) {
            CLI.print("No cashiers found.");
            return applied;
        }
        else {
            CLI.print("Cashiers:");
            Collections.sort(CashManager.getCashList(), Comparator.comparing(Cash::getName));
            for (Cash cash : CashManager.getCashList()) {
                CLI.print("  " + cash.toString());
            }
            return applied;
        }
    }
}





