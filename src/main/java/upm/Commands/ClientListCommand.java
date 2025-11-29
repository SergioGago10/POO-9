package upm.Commands;

import upm.CLI;
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
            CLI.print("No clients found.");
            return applied;
        }
        else {
            CLI.print("Clients:");
            Collections.sort(ClientsManager.getClientsList(),Comparator.comparing(Client::getName));
            for (Client client : ClientsManager.getClientsList()) {
                CLI.print("  " + client.toString());
            }
            return applied;
        }

    }

}
