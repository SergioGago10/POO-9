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
        try {
            if (args.length < 3) {
                CLI.printErrorNextLine("Error -> Format must be: client remove <DNI>");
                return true;
            }

            UserManager userManager=UserManager.getInstance();
            String dni = args[2];
            Client client = (Client) userManager.getUserByID(dni);

            if (client == null) {
                CLI.printErrorNextLine("Error -> Client not found.");
                return true;
            }

            if (!userManager.removeUserByDni(dni)) {
                CLI.printErrorNextLine("Error -> Client couldn't be removed.");
                return true;
            }

            CLI.printNextLine("client remove: ok");
        } catch (ClassCastException ex) {
            CLI.printErrorNextLine("Error -> Id doesnt belong to a Client, it belongs to a cashier.");
        }
        return true;
    }
}
