package upm.Commands;

import upm.tickets.TicketManager;

public class TicketListCommand extends TicketCommand{

    public TicketListCommand(TicketManager ticketManager){
        super("list",ticketManager);
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        try{
            if(args.length!=2){
                System.err.println("Usage: ticket list");
            }else{
                ticketManager.printListTickets();
                System.out.println("ticket list: ok");
            }
        } catch (Exception e) {
            System.err.println("Error printing all tickets: " + e.getMessage());
        }
        return applied;
    }
}
