package upm.Commands;

import upm.tickets.TicketManager;

public class TicketNewCommand extends TicketCommand {

    public TicketNewCommand(TicketManager ticketManager){
        super("new",ticketManager);
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        if(args.length<4 || args.length>5){
            System.out.println("Usage: ticket new [<id>] <cashId> <userId> ");
        } else if (args.length == 4) {
            ticketManager.newTicket(Integer.parseInt(args[2]),Integer.parseInt(args[3]));
            applied = true;
        } else{
            ticketManager.newTicket(args[2],Integer.parseInt(args[3]),Integer.parseInt(args[4]));
            applied = true;
        }
        System.out.println("ticket new: ok");
        return applied;
    }

}
