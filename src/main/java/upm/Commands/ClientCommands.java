package upm.Commands;

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
        if (message.length > 0) {
            String subCommand = message[1];
            for (Command cmd : clientcommands) {
                if (!applied || cmd.getText().equalsIgnoreCase(subCommand)) {
                    applied = cmd.apply(message);
                }
            }
        }
        return applied;

    }
}
