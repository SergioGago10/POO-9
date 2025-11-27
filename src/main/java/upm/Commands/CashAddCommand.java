package upm.Commands;
import upm.CLI;
import upm.Users.Cash;
import upm.Users.CashManager;
import upm.Users.ClientsManager;


public class CashAddCommand extends Command {
    public CashAddCommand() {
        super("add");

    }

    public boolean apply(String[] args) {
        boolean applied = false;
        if (args.length < 4) {
            CLI.print("Format must be: prod add [<id>] \"<name>\" [<email>]");
            return false;
        } else {
            try {
                for (int k = 0; k < args.length; k++) {
                    args[k] = args[k].replace("\"", "")
                            .replace("“", "")
                            .replace("”", "")
                            .trim();
                }
                int i = 2;
                String id;
                String name;
                String email;
                if (args[i].matches("[A-Za-z0-9]+")) {
                    id = args[i];
                    i++;
                } else {
                    id = CashManager.generateRandomId();
                }

                name = args[i];
                i++;

                email = args[i];

                Cash cash = new Cash(id, name, email);
                if (CashManager.addCash(cash)) {
                    System.out.println(cash);
                    System.out.println("cash add: ok");
                    applied = true;
                }

                } catch(Exception e){
                    CLI.print("Error adding cash user.");
                    return false;
                }
            }

        return applied;
    }
}




