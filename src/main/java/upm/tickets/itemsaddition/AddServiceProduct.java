package upm.tickets.itemsaddition;

import upm.CLI;
import upm.products.*;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;

import java.time.LocalDateTime;

public class AddServiceProduct extends ItemAdditionStrategy {
    private TicketManager ticketManager = TicketManager.getInstance();

    @Override
    public boolean add(ProductService service, String[] args) {
        Ticket<?> ticket = this.ticketManager.getTicketById(args[0]);
        boolean isDateValid = service.getMaxDate().isAfter(LocalDateTime.now());
        boolean isServiceInTicket = ticket.getItemsList().contains(service);
        if(isServiceInTicket){
            CLI.printErrorNextLine("Error -> The service is already in the ticket.");
            return false;
        }
        if(!isDateValid){
            CLI.printErrorNextLine("Error -> The service date has expired.");
            return false;
        }
        return  ticket.tryToAdd(service);
    }

}
