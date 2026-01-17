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
        }else if(args.length == 5) {
            if(args[4].startsWith("-")) {
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

        UserManager userManager=UserManager.getInstance();
        if (!userManager.idExists(cashId)) {
            CLI.printErrorNextLine("Error -> Cashier ID does not exist: " + cashId);
            return true;
        }

        if (!userManager.idExists(userId)) {
            CLI.printErrorNextLine("Error -> Client DNI does not exist: " + userId);
            return true;
        }

        if(!isOptionValid(option)){
            CLI.printErrorNextLine("Error -> Option provided does not exist: " + option);
            return true;
        }

        //Determinamos si el user es empresa o usuario normal, poniendo las restricciones convenientes.
        //Si es user, usa DNI, los DNI acaban siempre con una letra, las empresas no, ya que usan un NIF
        //el NIF empieza por una letra, pero no termina en una
        User user = userManager.getUserByID(userId);
        if(user.getId().matches(".*[A-Za-z]$") && !option.contentEquals("-p")){
            CLI.printErrorNextLine("Error -> An user can not create a ticket of type '-c' or '-s' only company users are able to.");
            return true;
        } else if(!user.getId().matches(".*[A-Za-z]$") && option.contentEquals("-p")){
            CLI.printErrorNextLine("Error -> A company user can not create a ticket of type '-p' only users are able to.");
            return true;
        }

        try {
            Cash cashier = (Cash) userManager.getUserByID(cashId);
            Client client = (Client) userManager.getUserByID(userId);
            TicketManager ticketManager=TicketManager.getInstance();
            if (ticketId == null) {
                ticket = ticketManager.newTicket(option);
            } else {
                ticket = ticketManager.newTicket(ticketId,option);
            }

            cashier.addTicket(ticket);
            client.addTicket(ticket);
            TicketFormatter ticketFormatter = new TicketFormatter();
            ticketFormatter.printCurrentTicket(ticket);
            CLI.printNextLine("ticket new: ok");
        }catch (ClassCastException ex){
            CLI.printErrorNextLine("Error -> First id must be a cash id and second id must be a client DNI.");
        }
        return true;
    }

    private boolean isOptionValid(String option){
        return Objects.equals(option, "-c") || Objects.equals(option, "-p") || Objects.equals(option, "-s");
    }
}
