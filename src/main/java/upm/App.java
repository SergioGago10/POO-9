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
import java.util.Locale;

public class App {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); //Queremos que se ponga con pnuto los numeros por el formato que nos dan
        App app = new App();
        app.init();
        app.run(args);
        app.close();
    }

    private void init() {
        System.out.println("Welcome to the ticket module App.");
    }

    private void run(String[] args) {
        CLI cli = new CLI(args);
        boolean running = true;
        boolean isInteractive = (args.length != 0); // true si se pasa archivo, false si escribimos por la consola
        System.setErr(System.out);
        System.out.println("Ticket module. Type 'help' to see commands.");

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
            CLI.printText("tUPM> ");
            String[] userInput = CLI.nextLine(isInteractive); // El trim evita espacios al principio y al final

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
                            handled = true;
                            break; // ya hay un comando que ha ejecutado esta línea
                        }
                    } catch (Exception e) {
                        System.err.print("Error: " + e.getMessage());
                        handled = true; // consideramos la línea “gestionada” aunque sea con error
                        break;
                    }
                }

                if (!handled) {
                    System.err.println("Command not found. Type 'help' to see the command list.");
                }

                System.out.println();
            }
        }
    }

    private void close() {
        CLI.print("Closing application.\nGoodbye!");
        CLI.closeSc();
        System.exit(0);
    }

}
