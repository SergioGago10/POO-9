package upm.Commands;

import upm.Users.Cash;
import upm.Users.CashManager;
import upm.Users.Client;
import upm.Users.ClientsManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClientListCommand extends Command {
    public ClientListCommand() {
        super("list");
    }
    @Override
    public boolean apply(String[] args) {
        boolean applied = true;
        if (ClientsManager.getClientsList().size()==0) {
            System.out.println("No clients found.");
            return applied;
        }
        else {
            System.out.println("Clients:");
            for (Client client : ClientsManager.getClientsList()) {
                System.out.println("  " + client.toString());
            }
            return applied;
        }

    }

}
