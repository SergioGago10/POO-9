package upm.Commands;

import upm.Users.Cash;
import upm.Users.UserManager;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;
import upm.tickets.TicketState;

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
                TicketManager ticketManager=TicketManager.getInstance();
                Ticket<?> ticketAMostrar = ticketManager.getTicketById(ticketId);
                if(ticketAMostrar != null){
                    Cash cashUser = (Cash) UserManager.getInstance().getUserByID(cashId);
                    if(!cashUser.getTickets().contains(ticketAMostrar)){
                        System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                    } else{
                        //todo revisar el hecho de cerrar aqui o en otro lado
                        if(ticketAMostrar.getEstado() != TicketState.EMPTY){
                            ticketAMostrar.closeTicket();
                        }
                        ticketManager.getFormatter().printFinalTicket(ticketAMostrar);
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
