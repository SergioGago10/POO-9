package upm.Commands;

import upm.CLI;
import upm.Users.Client;
import upm.Users.ClientsManager;
import upm.Utilities;

import java.util.Iterator;

public class ClientRemoveCommand extends Command {
    public ClientRemoveCommand(){
        super("remove");
    }
    @Override
    public boolean apply(String[] args) {
        boolean applied;

        if (args.length < 3) {
            System.out.println("Format must be: client remove <DNI>");
            applied = false;
        } else {
            String dni = args[2];
            Client client = ClientsManager.getClientByDni(dni);

            if (ClientsManager.removeClient(client)) {
                CLI.print("client remove: ok");
                applied = true;
            } else {
                CLI.print("Client not found or couldn't be removed.");
                applied = false;
            }
        }
        return applied;
    }


}
