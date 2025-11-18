package upm.Commands;
import upm.Users.Cash;
import upm.Users.CashManager;



    public class CashAddCommand extends Command {
        public CashAddCommand() {
            super("add");

        }

        public boolean apply(String[] args) {
            boolean applied;
            if (args.length < 4) {
                return false;
            }
            try {
                int i = 2;
                String id;
                String name;
                String email;
                if (args[i].contains("\"")) {
                    id = CashManager.generateRandomId();
                } else {
                    id = args[i];
                    i++;
                }

                name = args[i].replace("\"", "");
                i++;

                email = args[i];

                Cash cash = new Cash(id, name, email);

                CashManager.addCash(cash);
                System.out.println(cash);

                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
