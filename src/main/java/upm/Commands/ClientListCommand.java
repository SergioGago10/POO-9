package upm.Commands;

import upm.Users.Client;
import upm.Users.ClientsManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClientListCommand extends Command {
    public ClientListCommand(String text) {
        super("List");
    }
    @Override
    public boolean apply(String[] args) {
        boolean applied;

        if (args.length < 2) {
            applied = false;
        } else {
            ClientsManager.showClients(ClientsManager.clientsList);
            applied=true;
        }

        return applied;
    }

}
