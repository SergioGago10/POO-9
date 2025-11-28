package upm.Commands;
import upm.CLI;
import upm.Users.Cash;
import upm.Users.CashManager;

public class CashListCommand extends Command {
    public CashListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {

        CLI.print("Cash:");

        for (Cash cash : CashManager.getCashList()) {
            CLI.print("  " + cash.toString());
        }

        return true;
    }
}





