package upm.commands.client;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Client;
import upm.users.UserManager;

import java.util.ArrayList;
import java.util.Comparator;

public class ClientListCommand extends Command {
    public ClientListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        UserManager userManager=UserManager.getInstance();
        ArrayList<Client> clientsList = userManager.getClients();

        if (clientsList.isEmpty()) {
            CLI.print("No clients found.");
            return true;
        }

        clientsList.sort(Comparator.comparing(Client::getName));

        CLI.printNextLine("Clients:");
        for (Client client : clientsList) {
            CLI.printNextLine("  " + client.toString());
        }

        CLI.printNextLine("Client list: ok.");
        return true;
    }
}
