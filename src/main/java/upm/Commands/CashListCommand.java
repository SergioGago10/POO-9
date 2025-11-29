package upm.Commands;

import upm.CLI;
import upm.Users.Cash;
import upm.Users.CashManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CashListCommand extends Command {
    public CashListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {

        ArrayList<Cash> list = new ArrayList<>(CashManager.getCashList());

        if (list.isEmpty()) {
            CLI.print("No cashiers found.");
            return true;
        }

        Collections.sort(list, Comparator.comparing(Cash::getName));

        CLI.print("Cashiers:");
        for (Cash cash : list) {
            CLI.print("  " + cash.toString());
        }

        return true;
    }
}



