package upm;

import upm.Commands.*;
import upm.tickets.TicketManager;

import java.util.ArrayList;
import java.util.List;

public class App {
    //Para controlar Exit
    private boolean running = true;


    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.run(args);
        app.close();
    }

    private void init() {
        System.out.println("Welcome to the ticket module App.");
    }

    private void run(String[] args) {
        CLI cli=new CLI(args);
        boolean isInteractive = (args.length != 0); // true si se pasa archivo, false si escribimos por la consola
        System.setErr(System.out);

        TicketManager ticketManager = new TicketManager(); // Antes de iniciar el programa se crea un ticketManager

        System.out.println("Ticket module. Type 'help' to see commands.");

        // 1) Lista de comandos
        List<Command> commands = new ArrayList<>();
        commands.add(new ProdCommands());
        commands.add(new CommandHelp());
        commands.add(new CommandEcho());
        commands.add(new ClientCommands());
        commands.add(new TicketCommands());

        // 2) Bucle principal de CLI
        while (running) {
            CLI.print("tUPM> ");
            String[] userInput = CLI.nextLine(isInteractive); // El trim evita espacios al principio y al final

            if (userInput[0].isEmpty()) {
                // Ignoramos líneas vacías
                continue;
            }
            boolean handled = false; // indica si algún comando ha gestionado la entrada

            for (Command command : commands) {
                try {
                    if (command.apply(userInput)) {
                        handled = true;
                        break; // ya hay un comando que ha ejecutado esta línea
                    }
                } catch (Exception e) {
                    System.err.println("Unexpected error: " + e.getMessage());
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

    private void close() {
        CLI.print("Closing application.\nGoodbye!");
        CLI.closeSc();
        System.exit(0);
    }

    private void helpCommand() {
        System.out.println("Commands:\n prod add <id> \"<name>\" <category> <price>\n" +
                " prod list\n" +
                " prod update <id> NAME|CATEGORY|PRICE <value>\n" +
                " prod remove <id>\n" +
                " ticket new\n" +
                " ticket add <prodId> <quantity>\n" +
                " ticket remove <prodId>\n" +
                " ticket print\n" +
                " echo \"<texto>\"\n" +
                " help\n" +
                " exit\n\n" + "Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS\n" +
                "Discounts if there are ≥2 units in the category: MERCH 0%," +
                " STATIONERY 5%, CLOTHES 7%, BOOK 10%,\n" +
                "ELECTRONICS 3%.");
    }

}
