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

        if (args.length < 3) {
            System.out.println("Format must be: cash add [<identifier>] \"<name>\" [<email>]");
            return false;
        }

        try {
            int i = 2;
            String identifier;
            String name;
            String email = "";

            if (args[i].startsWith("\"")) {
                identifier = CashManager.generateRandomIdentifier();
                name = args[i];
                i++;
                if (i < args.length) email = args[i];
            } else {
                identifier = args[i];
                i++;
                if (i >= args.length) {
                    System.out.println("Format must be: cash add [<identifier>] \"<name>\" [<email>]");
                    return false;
                }
                name = args[i];
                i++;
                if (i < args.length) email = args[i];
            }

            if (!(name.startsWith("\"") && name.endsWith("\""))) {
                System.out.println("The name must be enclosed in quotes.");
                return false;
            }

            identifier = identifier.replace("\"", "").trim();
            name = name.replace("\"", "").trim();
            email = email.replace("\"", "").trim();

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
