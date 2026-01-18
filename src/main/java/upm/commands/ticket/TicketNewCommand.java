package upm.commands.ticket;

import upm.CLI;
import upm.commands.core.Command;
import upm.tickets.format.TicketFormatter;
import upm.users.*;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;

import java.util.Objects;

public class TicketNewCommand extends Command {

    public TicketNewCommand() {
        super("new");
    }

    @Override
    public boolean apply(String[] args) {

        if (args.length < 4 || args.length > 6) {
            CLI.printErrorNextLine("Error -> format must be: ticket new [<id>] <cashId> <userId> -[c|p|s] (default -p option) ");
            return true;
        }

        // Inicializamos todos los valores
        String ticketId = null;
        String cashId;
        String userId;
        String option;
        Ticket<?> ticket;

        if (args.length == 4) {
            cashId = args[2];
            userId = args[3];
            option = "-p";
        } else if (args.length == 5) {
            if (args[4].startsWith("-")) {
                cashId = args[2];
                userId = args[3];
                option = args[4];
            } else {
                ticketId = args[2];
                cashId = args[3];
                userId = args[4];
                option = "-p";
            }
        } else {
            ticketId = args[2];
            cashId = args[3];
            userId = args[4];
            option = args[5];
        }

        if (!isOptionValid(option)) {
            CLI.printErrorNextLine("Error -> Option provided does not exist: " + option);
            return true;
        }

        UserManager userManager = UserManager.getInstance();

        // Validaciones robustas: primero recuperar objetos, luego comprobar tipos
        User cashUser = userManager.getUserByID(cashId);
        if (!(cashUser instanceof Cash cashier)) {
            CLI.printErrorNextLine("Error -> Cashier ID does not exist: " + cashId);
            return true;
        }

        User clientUser = userManager.getUserByID(userId);
        if (!(clientUser instanceof Client client)) {
            CLI.printErrorNextLine("Error -> Client DNI does not exist: " + userId);
            return true;
        }

        // Validar que el id no sea null (evita NPEs tipo user.getId().matches / equals)
        String clientId = client.getId();
        if (clientId == null || clientId.isBlank()) {
            CLI.printErrorNextLine("Error -> Client has null/empty id. Persistence data may be corrupted (system.json).");
            return true;
        }

        // Restricciones por tipo de usuario (DNI termina en letra; empresa no termina en letra)
        boolean endsWithLetter = clientId.matches(".*[A-Za-z]$");

        if (endsWithLetter && !Objects.equals(option, "-p")) {
            CLI.printErrorNextLine("Error -> An user can not create a ticket of type '-c' or '-s' only company users are able to.");
            return true;
        } else if (!endsWithLetter && Objects.equals(option, "-p")) {
            CLI.printErrorNextLine("Error -> A company user can not create a ticket of type '-p' only users are able to.");
            return true;
        }

        try {
            TicketManager ticketManager = TicketManager.getInstance();

            if (ticketId == null) {
                ticket = ticketManager.newTicket(option);
            } else {
                ticket = ticketManager.newTicket(ticketId, option);
            }

            cashier.addTicket(ticket);
            client.addTicket(ticket);

            TicketFormatter ticketFormatter = new TicketFormatter();
            ticketFormatter.printCurrentTicket(ticket);
            CLI.printNextLine("ticket new: ok");

        } catch (Exception ex) {
            CLI.printErrorNextLine("Error -> Could not create ticket: " + ex.getMessage());
        }

        return true;
    }

    private boolean isOptionValid(String option) {
        return Objects.equals(option, "-c") || Objects.equals(option, "-p") || Objects.equals(option, "-s");
    }
}
