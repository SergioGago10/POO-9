package upm.Commands;

import upm.CLI;
import upm.Users.Cash;
import upm.Users.CashManager;

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

        int i = 2;

        if (args[i].startsWith("\"") && args[i].endsWith("\"")) {
            identifier = CashManager.generateRandomIdentifier();
            rawName = args[i];
            i++;
            if (i < args.length) {
                email = args[i];
            }
            else {
                System.out.println("Format must be: cash add [<identifier>] \"<name>\" <email>");
                return false;
            }
        } else {
            identifier = args[i];
            i++;
            if (i >= args.length) {
                System.out.println("Format must be: cash add [<identifier>] \"<name>\" <email>");
                return false;
            }
            rawName = args[i];
            i++;
            if (i >= args.length) {
                System.out.println("Format must be: cash add [<identifier>] \"<name>\" <email>");
                return false;
            }
            email = args[i];
        }

        if (!(rawName.startsWith("\"") && rawName.endsWith("\""))) {
            CLI.print("The name must be enclosed in quotes.");
            return false;
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

            if (CashManager.addCash(cash)) {
                System.out.println(cash.toString());
                System.out.println("cash add: ok");
                applied = true;
            } else {
                System.err.println("Cashier couldn't be added.");
                applied = false;
            }
        } catch (Exception ex) {
            System.out.println("Error: Invalid parameters.");
            applied = false;
        }

        return applied;
    }
}
