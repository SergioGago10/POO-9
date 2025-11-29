package upm.Commands;

import upm.tickets.TicketManager;

public class TicketListCommand extends Command{

    public TicketListCommand(){
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        try{
            if(args.length!=2){
                System.err.println("Usage: ticket list");
            }else{
                TicketManager.printListTickets();
                System.out.println("ticket list: ok");
                applied = true;
            }
        } catch (Exception e) {
            System.err.println("Error printing all tickets: " + e.getMessage());
        }
        return applied;
    }
}
