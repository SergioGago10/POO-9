package upm;

import upm.commands.cash.CashCommands;
import upm.commands.client.ClientCommands;
import upm.commands.common.CommandEcho;
import upm.commands.common.CommandHelp;
import upm.commands.core.Command;
import upm.commands.product.ProdCommands;
import upm.commands.ticket.TicketCommands;
import upm.json.PersistenceService;

import java.util.*;

public class App {

    private final PersistenceService persistence = new PersistenceService();

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.run(args);
        app.close();
    }

    private void init() {
        CLI.printNextLine("Welcome to the TiendaUPM app, will you load a JSON file? [Y/N]");
        Scanner scanner = new Scanner(System.in);
        boolean correctDecision = false;

        while(!correctDecision){
            char userDecision = scanner.next().toUpperCase().charAt(0);
            if (userDecision == 'Y') {
                persistence.load();
                correctDecision = true;
            } else if (userDecision == 'N') {
                PersistenceService.reset(true);
                correctDecision = true;
            } else {
                CLI.printErrorNextLine("Invalid option. Please enter Y or N.");
            }
        }

        Locale.setDefault(Locale.ENGLISH);
        CLI.printNextLine("Welcome to the ticket module App.");
    }

    private void run(String[] args) {
        CLI cli = new CLI(args);
        boolean running = true;
        CLI.printNextLine("Ticket module. Type 'help' to see commands.");

        List<Command> commands = new ArrayList<>();
        commands.add(new CashCommands());
        commands.add(new ProdCommands());
        commands.add(new CommandHelp());
        commands.add(new CommandEcho());
        commands.add(new ClientCommands());
        commands.add(new TicketCommands());

        while (running) {
            try{
                CLI.print("tUPM> ");
                String[] userInput = cli.nextLine();

                if (userInput[0].isEmpty()) {
                    continue;
                }

                if (userInput[0].equals("exit")){
                    running = false;
                } else {
                    boolean handled = false;

                    for (Command command : commands) {
                        try {
                            if (userInput[0].equals(command.getText()) && command.apply(userInput)) {
                                handled = true;
                                break;
                            }
                        } catch (Exception e) {
                            CLI.printErrorNextLine("Error: " + e.getMessage());
                            handled = true;
                            break;
                        }
                    }
                    if (!handled) {
                        CLI.printErrorNextLine("Command not found. Type 'help' to see the command list.");
                    }

                    CLI.printNextLine("");
                }
            } catch (NoSuchElementException e){
                if(!cli.isInteractive()){
                    CLI.printNextLine("");
                    CLI.printErrorNextLine("The .txt has ended without an \"exit\" on his final line.");
                    CLI.printErrorNextLine("Switching to interactive mode. ");
                    CLI.printNextLine("");
                    cli.switchToInteractive();
                } else {
                    CLI.printErrorNextLine("An error has ocurred while running the app: " + e.getMessage());
                    running = false;
                }
            } finally {
                persistence.save();
            }
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
