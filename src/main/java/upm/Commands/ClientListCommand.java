package upm.Commands;

import upm.CLI;
import upm.Users.Client;
import upm.Users.ClientsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClientListCommand extends Command {
    public ClientListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        ArrayList<Client> clientsList = ClientsManager.getClientsList();

        if (clientsList.isEmpty()) {
            CLI.print("No clients found.");
            return true;
        }

        Collections.sort(clientsList, Comparator.comparing(Client::getName));

        CLI.print("Clients:");
        for (Client client : clientsList) {
            CLI.print("  " + client.toString());
        }

        CLI.print("Client list: ok.");
        return true;
    }
}
