package upm.commands.client;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Client;
import upm.users.TypeClient;
import upm.users.UserManager;

public class ClientAddCommand extends Command {
    public ClientAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        UserManager userManager = UserManager.getInstance();
        if (args.length < 6) {
            CLI.printErrorNextLine("Error -> Format must be: client add \"<name>\" <DNI> <email> <cashId>");
        }

        String rawName = args[2];
        if (!(rawName.startsWith("\"") && rawName.endsWith("\""))) {
            CLI.printNextLine("Error -> The name must be enclosed in quotes.");
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
            String identificator = args[3];
            String email = args[4];
            String cashierId = args[5];
            TypeClient type;

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                CLI.printErrorNextLine("Error -> Invalid email format.");
            }

            if (!userManager.idExists(cashierId)) {
                CLI.printErrorNextLine("Error -> Cashier ID does not exist.");
            }
            if (Character.isDigit(identificator.charAt(identificator.length() - 1)))
                type = TypeClient.COMPANY;
            else
                type = TypeClient.CLIENT;
            Client client = new Client(name, identificator, email, cashierId,type);

            if (userManager.addClient(client)) {
                CLI.printNextLine(client.toString());
                CLI.printNextLine("client add: ok");
                applied = true;
            } else {
                CLI.printErrorNextLine("Error -> Client could not be added.");
            }

        } catch (Exception ex) {
            CLI.printErrorNextLine("Error -> client could not be added: " + ex.getMessage());
        }

        return applied;
    }
}


