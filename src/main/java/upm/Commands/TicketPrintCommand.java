package upm.Commands;

import upm.tickets.Ticket;
import upm.tickets.TicketManager;

public class TicketPrintCommand extends TicketCommand{

    public TicketPrintCommand(TicketManager ticketManager){
        super("print",ticketManager);
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        try {
            if(args.length!= 4){
                System.err.println("Usage: ticket print <ticketId> <cashId>");
            } else{
                String ticketId = args[2];
                int cashId = Integer.parseInt(args[3]);
                Ticket ticketAMostrar = ticketManager.getTicketById(ticketId);
                if(ticketAMostrar != null){
                    if(ticketAMostrar.getCashId() != cashId){
                        System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                    } else{
                        ticketAMostrar.printFinalTicket();
                        System.out.println("ticket print: ok");
                        applied = true;
                    }
                } else{
                    System.err.println("Error: Ticket " + ticketId + " does not exist.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error printing ticket: " + e.getMessage());
        }
        return applied;
    }

}
