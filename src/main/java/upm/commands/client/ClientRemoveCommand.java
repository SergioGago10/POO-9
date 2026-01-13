package upm.commands.client;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Client;
import upm.users.UserManager;

public class ClientRemoveCommand extends Command {
    public ClientRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied=false;
        try {
            if (args.length < 3) {
                System.out.println("Format must be: client remove <DNI>");
            } else {
                UserManager userManager=UserManager.getInstance();
                String dni = args[2];
                Client client = (Client) userManager.getUserByID(dni);

                if (client == null) {
                    CLI.print("Client not found.");
                } else if (userManager.removeUserByDni(dni)) {
                    //CLI.print(client.toString()); no queremos que se haga print
                    CLI.print("client remove: ok");
                    applied = true;
                } else {
                    CLI.print("Client couldn't be removed.");
                }
            }

        } catch (ClassCastException ex) {
            CLI.print("Id doesnt belong to a Client, it belongs to a cashier.");
        }
        return applied;
    }
}
