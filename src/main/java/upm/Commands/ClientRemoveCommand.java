package upm.Commands;

import upm.CLI;
import upm.Users.Client;
import upm.Users.ClientsManager;

public class ClientRemoveCommand extends Command {
    public ClientRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied;

        if (args.length < 3) {
            System.out.println("Format must be: client remove <DNI>");
            applied = true;
        } else {
            String dni = args[2];
            Client client = ClientsManager.getClientByDni(dni);

            if (client == null) {
                CLI.print("Client not found.");
                applied = true;
            } else if (ClientsManager.removeClientByDni(dni)) {
                //CLI.print(client.toString()); no queremos que se haga print
                CLI.print("client remove: ok");
                applied = true;
            } else {
                CLI.print("Client couldn't be removed.");
                applied = true;
            }
        }

        return applied;
    }
}
