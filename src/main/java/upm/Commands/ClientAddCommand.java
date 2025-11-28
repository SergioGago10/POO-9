package upm.Commands;

import upm.CLI;
import upm.Users.CashManager;
import upm.Users.Client;
import upm.Users.ClientsManager;

public class ClientAddCommand extends Command {
    public ClientAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied;

        if (args.length < 6) {
            System.out.println("Format must be: client add \"<name>\" <DNI> <email> <cashId>");
            return false;
        }

        String rawName = args[2];
        if (!(rawName.startsWith("\"") && rawName.endsWith("\""))) {
            CLI.print("The name must be enclosed in quotes.");
            return false;
        }

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

            Integer.parseInt(cashierId);

            Client client = new Client(name, dni, email, cashierId);

            if (CashManager.idExists(cashierId) && ClientsManager.addClient(client)) {
                System.out.println(client.toString());
                System.out.println("client add: ok");
                applied = true;
            } else {
                CLI.print("Client is null or couldn't be added. Check the cashier ID.");
                applied = false;
            }
        } catch (NumberFormatException ex) {
            CLI.print("CashierId must be an integer number and must already exist.");
            applied = false;
        }

        return applied;
    }
}
