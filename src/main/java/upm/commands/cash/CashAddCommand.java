package upm.commands.cash;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Cash;
import upm.users.UserManager;

public class CashAddCommand extends Command {
    public CashAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        if (args.length < 4) {
            System.out.println("Format must be: cash add [<identifier>] \"<name>\" <email>");
            return false;
        }

        String identifier;
        String rawName;
        String email = "";
        UserManager userManager=UserManager.getInstance();
        int i = 2;

        if (args[i].startsWith("\"") && args[i].endsWith("\"")) {
            identifier = userManager.generateRandomIdentifier();
            rawName = args[i];
            i++;
            email = args[i];
        } else {
            identifier = args[i];
            i++;
            rawName = args[i];
            i++;
            if (i >= args.length) {
                System.out.println("Format must be: cash add [<identifier>] \"<name>\" <email>");
                return true;
            }
            email = args[i];
        }

        if (!(rawName.startsWith("\"") && rawName.endsWith("\""))) {
            CLI.print("The name must be enclosed in quotes.");
            return true;
        }

        try {
            identifier = identifier.replace("\"", "")
                    .replace("“", "")
                    .replace("”", "")
                    .trim();

            String name = rawName.replace("\"", "")
                    .replace("“", "")
                    .replace("”", "")
                    .trim();

            email = email.replace("\"", "")
                    .replace("“", "")
                    .replace("”", "")
                    .trim();

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                CLI.print("Invalid email format.");
                return false;
            }

            Cash cash = new Cash(identifier, name, email);

            if (userManager.addCash(cash)) {
                System.out.println(cash.toString());
                System.out.println("cash add: ok");
                applied = true;
            } else {
                System.err.println("Cashier couldn't be added.");
            }
        } catch (Exception ex) {
            System.out.println("Error: Invalid parameters.");
        }

        return applied;
    }
}
