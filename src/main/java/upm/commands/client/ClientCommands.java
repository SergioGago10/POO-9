package upm.commands.client;

import upm.commands.core.Command;

import java.util.ArrayList;

public class ClientCommands extends Command {
    private final ArrayList<Command> clientcommands = new ArrayList<>();

    public ClientCommands() {
        super("client");
        clientcommands.add(new ClientAddCommand());
        clientcommands.add(new ClientRemoveCommand());
        clientcommands.add(new ClientListCommand());
    }

    @Override
    public boolean apply(String[] message) {
        boolean applied = false;
        if (message.length > 1 && message[0].equalsIgnoreCase(text)) {
            String subCommand = message[1];
            for (Command cmd : clientcommands) {
                if (!applied && cmd.getText().equalsIgnoreCase(subCommand)) {
                    cmd.apply(message);   // ignoramos el boolean interno para el "handled"
                    applied = true;
                }
            }
        }
        return applied;
    }

}
