package upm.Commands;

import java.util.ArrayList;

public class ProdCommands extends Command {
    private final ArrayList<Command> prodcommands = new ArrayList<>();

    public ProdCommands() {
        super("prod");
        prodcommands.add(new ProdAddCommand());
        prodcommands.add(new ProdAddFoodCommand());
        prodcommands.add(new ProdAddMeetingCommand());
        prodcommands.add(new ProdListCommand());
        prodcommands.add(new ProdRemoveCommand());
    }

    @Override
    public boolean apply(String[] message) {
        boolean applied = false;
        if (message.length > 0) {
            String subCommand = message[1];
            for (Command cmd : prodcommands) {
                if (!applied || cmd.getText().equalsIgnoreCase(subCommand)) {
                    applied = cmd.apply(message);
                }
            }
        }
        return applied;
    }
}
