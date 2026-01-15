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
        UserManager userManager = UserManager.getInstance();

        if (args.length < 6) {
            CLI.printErrorNextLine("Error -> Format must be: client add \"<name>\" <DNI> <email> <cashId>");
            return true;
        }

        String rawName = args[2];
        if (!(rawName.startsWith("\"") && rawName.endsWith("\""))) {
            CLI.printErrorNextLine("Error -> The name must be enclosed in quotes.");
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
            String identificator = args[3];
            String email = args[4];
            String cashierId = args[5];
            TypeClient type;

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                CLI.printErrorNextLine("Error -> Invalid email format.");
                return true;
            }

            if (!userManager.idExists(cashierId)) {
                CLI.printErrorNextLine("Error -> Cashier ID does not exist.");
                return true;
            }

            if (Character.isDigit(identificator.charAt(identificator.length() - 1))) {
                type = TypeClient.COMPANY;
            } else {
                type = TypeClient.CLIENT;
            }

            Client client = new Client(name, identificator, email, cashierId,type);

            if (!userManager.addClient(client)) {
                CLI.printErrorNextLine("Error -> Client could not be added.");
                return true;
            }

            CLI.printNextLine(client.toString());
            CLI.printNextLine("client add: ok");
        } catch (Exception ex) {
            CLI.printErrorNextLine("Error -> client could not be added: " + ex.getMessage());
        }

        return true;
    }
}


