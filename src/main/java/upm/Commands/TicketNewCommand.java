package upm.Commands;

import upm.CLI;
import upm.Users.*;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;

import java.util.Objects;

public class TicketNewCommand extends Command {

    public TicketNewCommand() {
        super("new");
    }

    @Override
    public boolean apply(String[] args) {

        if (args.length < 4 || args.length > 6) {
            System.out.println("Usage: ticket new [<id>] <cashId> < userId> -[c|p|s] (default -p option) ");
            return false;
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
        }else if(args.length == 5){
            if(args[4].startsWith("-")){
                cashId = args[2];
                userId = args[3];
                option = args[4];
            } else {
                ticketId = args[2];
                cashId = args[3];
                userId = args[4];
                option = "-p";
            }
        } else{
            ticketId = args[2];
            cashId = args[3];
            userId = args[4];
            option = args[5];
        }

        UserManager userManager=UserManager.getInstance();
        if (!userManager.idExists(cashId)) {
            CLI.print("Cashier ID does not exist: " + cashId);
            return false;
        }

        if (!userManager.idExists(userId)) {
            CLI.print("Client DNI does not exist: " + userId);
            return false;
        }

        if(!isOptionValid(option)){
            CLI.print("Option provided does not exist: " + option);
            return false;
        }

        try {

            Cash cashier = (Cash) userManager.getUserByID(cashId);
            Client client = (Client) userManager.getUserByID(userId);
            TicketManager ticketManager=TicketManager.getInstance();
            if (ticketId == null) {
                ticket = ticketManager.newTicket(cashId, userId, option);
            } else {
                ticket = ticketManager.newTicket(ticketId, cashId, userId, option,false);
            }

            cashier.addTicket(ticket);
            client.addTicket(ticket);
            CLI.print("ticket new: ok");
            return true;
        }catch (ClassCastException ex){
            CLI.print("First id must be a cash id and second id must be a client DNI.");
            return false;
        }

    }

    private boolean isOptionValid(String option){
        return Objects.equals(option, "-c") || Objects.equals(option, "-p") || Objects.equals(option, "-s");
    }
}
