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
            return true;
        }

        String rawName = args[2];
        if (!(rawName.startsWith("\"") && rawName.endsWith("\""))) {
            CLI.print("The name must be enclosed in quotes.");
            return true;
        }

        try {
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i]
                        .replace("\"", "")
                        .replace("“", "")
                        .replace("”", "")
                        .trim();
            }

            String name = args[2];
            String dni = args[3];
            String email = args[4];
            String cashierId = args[5];

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                CLI.print("Invalid email format.");
                return true;
            }

            if (!CashManager.idExists(cashierId)) {
                CLI.print("Cashier ID does not exist.");
                return true;
            }

            Client client = new Client(name, dni, email, cashierId);

            if (ClientsManager.addClient(client)) {
                System.out.println(client.toString());
                System.out.println("client add: ok");
                applied = true;
            } else {
                CLI.print("Client could not be added.");
                applied = true;
            }

        } catch (Exception ex) {
            CLI.print("Error adding client.");
            applied = true;
        }

        return applied;
    }
}


