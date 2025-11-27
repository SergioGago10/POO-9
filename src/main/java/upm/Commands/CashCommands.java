package upm.Commands;


import java.util.ArrayList;

public class CashCommands extends Command {
    private final ArrayList<Command> cashcommands = new ArrayList<>();

    public CashCommands() {
        super("cash");
        cashcommands.add(new CashAddCommand());
        cashcommands.add(new CashListCommand());
        cashcommands.add(new CashRemoveCommand());
        cashcommands.add(new CashTicketCommand());

    }

    @Override
    public boolean apply(String[] message) {
        boolean applied = false;
        if (message.length > 1 && message[0].equalsIgnoreCase(text)) {
            String subCommand = message[1];
            for (Command cmd : cashcommands) {
                if (!applied && cmd.getText().equalsIgnoreCase(subCommand)) {
                    cmd.apply(message);
                    applied = true;
                }
            }
        }
        return applied;

    }
}
