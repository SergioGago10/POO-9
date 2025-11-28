package upm.Commands;

import upm.Users.CashManager;
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
            System.out.println("Format must be: client add \"<name>\" <DNI> <email> <cashId> ");
            applied = false;
        } else {
            try {
                for (int i = 0; i < args.length; i++) {
                    args[i] = args[i].replace("\"", "")
                            .replace("“", "")
                            .replace("”", "")
                            .trim();
                }
                String name = args[2];
                String dni = args[3];
                String email = args[4];
                String cashierId = args[5];

                Client client = new Client(name, dni, email, cashierId);

                if (CashManager.idExists(cashierId) && ClientsManager.addClient(client)) {
                    System.out.println("client add: ok");
                    applied = true;
                } else {
                    System.err.println("Client is null or couldn't be added. Check the cashier ID.");
                    applied = false;
                }
            } catch (NumberFormatException ex) {
                System.err.println("CashierId must be an integer number and must already exist.");
                applied = false;
            }
        }

        return applied;
    }


}
