package upm;

import upm.commands.cash.CashCommands;
import upm.commands.client.ClientCommands;
import upm.commands.common.CommandEcho;
import upm.commands.common.CommandHelp;
import upm.commands.core.Command;
import upm.commands.product.ProdCommands;
import upm.commands.ticket.TicketCommands;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.run(args);
        app.close();
    }

    private void init() {
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
                // Ignoramos líneas vacías
                continue;
            }
            if (userInput[0].equals("exit"))
                running = false;
            else {
                boolean handled = false; // indica si algún comando ha gestionado la entrada

                for (Command command : commands) {
                    try {
                        if (userInput[0].equals(command.getText()) && command.apply(userInput)) {
                            // el booleano handled SIEMPRE será true si se ejecuta un comando
                            // da igual que sea error, lo importante es que el comando se ha encontrado y ejecutado
                            // la unica forma de la cual dará false es que el comando no se encuentre
                            handled = true;
                            break; // ya hay un comando que ha ejecutado esta línea
                        }
                    } catch (Exception e) {
                        CLI.printError("Error: " + e.getMessage());
                        handled = true; // consideramos la línea “gestionada” aunque sea con error
                        break;
                    }
                }

                if (!handled) {
                    CLI.printErrorNextLine("Command not found. Type 'help' to see the command list.");
                }

                CLI.printNextLine("");
            }
        }
    }

    private void close() {
        CLI.print("Closing application.\nGoodbye!");
        CLI.closeSc();
        System.exit(0);
    }

}
