package upm.Commands;

import upm.tickets.TicketManager;

public class TicketNewCommand extends Command {

    public TicketNewCommand(){
        super("new");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        if(args.length<4 || args.length>5){
            System.out.println("Usage: ticket new [<id>] <cashId> <userId> ");
        } else if (args.length == 4) {
            TicketManager.newTicket(Integer.parseInt(args[2].substring(2)),Integer.parseInt(args[3].substring(0, args[3].length() - 1)));
            applied = true;
        } else{
            TicketManager.newTicket(args[2],Integer.parseInt(args[3].substring(2)),Integer.parseInt(args[4].substring(0, args[4].length()-1)),false);

            applied = true;
        }
        System.out.println("ticket new: ok");
        return applied;
    }

}
