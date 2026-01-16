package upm;

import upm.commands.cash.CashCommands;
import upm.commands.client.ClientCommands;
import upm.commands.common.CommandEcho;
import upm.commands.common.CommandHelp;
import upm.commands.core.Command;
import upm.commands.product.ProdCommands;
import upm.commands.ticket.TicketCommands;
import upm.json.PersistenceService;

import java.util.ArrayList;
import java.util.List;

public class App {

    private final PersistenceService persistence = new PersistenceService();

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.run(args);
        app.close();
    }

    private void init() {
        // Si tu PersistenceService tiene load/read, actívalo:
        // persistence.load();

        CLI.printNextLine("Welcome to the ticket module App.");
    }

    private void run(String[] args) {
        CLI cli = new CLI(args);
        boolean running = true;
        CLI.printNextLine("Ticket module. Type 'help' to see commands.");

        // 1) Lista de comandos
        List<Command> commands = new ArrayList<>();
        commands.add(new CashCommands());
        commands.add(new ProdCommands());
        commands.add(new CommandHelp());
        commands.add(new CommandEcho());
        commands.add(new ClientCommands());
        commands.add(new TicketCommands());

        // 2) Bucle principal de CLI
        while (running) {
            CLI.print("tUPM> ");
            String[] userInput = cli.nextLine();

            if (userInput[0].isEmpty()) {
                continue; // ignoramos líneas vacías
            }

            if (userInput[0].equals("exit")) {
                running = false;
                continue;
            }

            boolean handled = false;

            for (Command command : commands) {
                try {
                    if (userInput[0].equals(command.getText()) && command.apply(userInput)) {
                        handled = true;
                        break;
                    }
                } catch (Exception e) {
                    CLI.printError("Error: " + e.getMessage());
                    handled = true;
                    break;
                }
            }

            if (!handled) {
                CLI.printErrorNextLine("Command not found. Type 'help' to see the command list.");
            }

            CLI.printNextLine("");
        }
    }

    private void close() {
        try {
            persistence.save();
        } catch (Exception e) {
            CLI.printErrorNextLine("Error saving persistence: " + e.getMessage());
        } finally {
            CLI.print("Closing application.\nGoodbye!");
            CLI.closeSc();
        }
    }
}

