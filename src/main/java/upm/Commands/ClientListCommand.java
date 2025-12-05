package upm.Commands;

import upm.CLI;
import upm.Products.ProductManager;
import upm.Users.Client;
import upm.Users.UserManager;

import java.util.ArrayList;
import java.util.Collections;
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

        Collections.sort(clientsList, Comparator.comparing(Client::getName));

        CLI.print("Clients:");
        for (Client client : clientsList) {
            CLI.print("  " + client.toString());
        }

        CLI.print("Client list: ok.");
        return true;
    }
}
