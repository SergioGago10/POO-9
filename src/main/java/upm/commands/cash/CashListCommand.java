package upm.commands.cash;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Cash;
import upm.users.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CashListCommand extends Command {
    public CashListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        UserManager userManager=UserManager.getInstance();
        ArrayList<Cash> list = new ArrayList<>(userManager.getCash());

        if (list.isEmpty()) {
            CLI.printNextLine("No cashiers found.");
            return true;
        }

        list.sort(Comparator.comparing(Cash::getName));

        CLI.printNextLine("Cashiers:");
        for (Cash cash : list) {
            CLI.printNextLine(cash.toString());
        }

        CLI.printNextLine("cash list: ok");
        return true;
    }
}



