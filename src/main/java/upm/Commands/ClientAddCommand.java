package upm.Commands;

import upm.Users.Client;
import upm.Users.ClientsManager;
import upm.Utilities;

import java.util.Iterator;

public class ClientAddCommand extends Command {
    public ClientAddCommand(){
        super("add");
    }
    @Override
    public boolean apply(String[] args) {
        boolean applied;

        if (args.length < 6) {
            System.out.println("Format must be: client add \"<name>\" <DNI> <email> <CashierId> ");
            applied = false;
        } else {
            try {
                int i = 2;
                String name = args[i];
                i++;
                String dni = args[i];
                i++;
                String email = args[i];
                i++;
                String cashierId = args[i];

                Client client = new Client(name, dni, email, cashierId);

                if (ClientsManager.addClient(client)) {
                    System.out.println("client add: ok");
                    applied = true;
                } else {
                    System.err.println("Client is null or couldn't be added.");
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
