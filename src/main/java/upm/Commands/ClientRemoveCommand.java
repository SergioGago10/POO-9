package upm.Commands;

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

        if (args.length < 6) {
            applied = false;
        } else {
            try {
                int i = 2;
                String name = args[i++];
                String dni = args[i++];
                String email = args[i++];
                int cashierId = Integer.parseInt(args[i]);

                Client client = new Client(name, dni, email, String.valueOf(cashierId));

                if (ClientsManager.removeClient(client)) {
                    System.out.println("client remove: ok");
                    applied = true;
                } else {
                    System.err.println("Client not found or couldn't be removed.");
                    applied = false;
                }
            } catch (NumberFormatException ex) {
                System.err.println("CashierId must be an integer number.");
                applied = false;
            }
        }

        return applied;
    }


}
