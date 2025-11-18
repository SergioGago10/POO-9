package upm.Commands;
import upm.Users.Cash;
import upm.Users.CashManager;

public class CashListCommand extends Command {
    public CashListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {

        System.out.println("Cash:");

        for (Cash cash : CashManager.getCashList()) {
            System.out.println("  " + cash.toString());
        }

        return true;
    }
}





