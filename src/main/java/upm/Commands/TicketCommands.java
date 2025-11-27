package upm.Commands;

import upm.tickets.TicketManager;
import java.util.ArrayList;

public class TicketCommands extends Command {
    private final ArrayList<Command> ticketcommands = new ArrayList<>();

    public TicketCommands() {
        super("ticket");
        ticketcommands.add(new TicketNewCommand());
        ticketcommands.add(new TicketAddCommand());
        ticketcommands.add(new TicketRemoveCommand());
        ticketcommands.add(new TicketPrintCommand());
        ticketcommands.add(new TicketListCommand());
    }

    @Override
    public boolean apply(String[] message) {
        boolean applied = false;
        if (message.length > 0 && message[0].equalsIgnoreCase(text) && message.length>=2) {
            String subCommand = message[1];
            for (Command cmd : ticketcommands) {
                if (!applied && cmd.getText().equalsIgnoreCase(subCommand)) {
                    applied = cmd.apply(message);
                }
            }
        }
        return applied;
    }

}
