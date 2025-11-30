package upm.Commands;

import upm.tickets.Ticket;
import upm.tickets.TicketManager;

public class TicketPrintCommand extends Command{

    public TicketPrintCommand(){
        super("print");
    }

    @Override
    public boolean apply(String[] args) {
        try {
            if(args.length!= 4){
                System.err.println("Usage: ticket print <ticketId> <cashId>");
            } else{
                String ticketId = args[2];
                String cashId = args[3];
                Ticket ticketAMostrar = TicketManager.getTicketById(ticketId);
                if(ticketAMostrar != null){
                    if(!ticketAMostrar.getCashId().equals(cashId)){
                        System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                    } else{
                        ticketAMostrar.printFinalTicket();
                        System.out.println("ticket print: ok");
                    }
                } else{
                    System.err.println("Error: Ticket " + ticketId + " does not exist.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error printing ticket: " + e.getMessage());
        }
        return true;
    }

}
