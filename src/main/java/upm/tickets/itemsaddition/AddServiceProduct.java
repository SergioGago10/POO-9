package upm.tickets.itemsaddition;

import upm.CLI;
import upm.products.Item;
import upm.products.ProductService;
import upm.tickets.core.Ticket;

import java.time.LocalDateTime;

public class AddServiceProduct extends ItemAdditionStrategy<ProductService> {

    @Override
    public boolean canHandle(Item product){
        return product instanceof ProductService;
    }

    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    public boolean canBeAdded(String[] args) {
        Ticket<?> ticket = this.ticketManager.getTicketById(args[0]);
        ProductService service = (ProductService) this.productManager.getIProduct(args[1]);
        boolean isDateValid = service.getMaxDate().isAfter(LocalDateTime.now());
        boolean isServiceInTicket = ticket.getItemsList().contains(service);
        if(isServiceInTicket){
            CLI.print("The service is already in the ticket.");
            return false;
        }
        if(!isDateValid){
            CLI.print("The service date has expired.");
            return false;
        }
        return true;
    }

    @Override
    protected boolean addMultipleTimes(String[] args) {
        Ticket<? extends Item> ticket =  this.ticketManager.getTicketById(args[0]);
        ProductService service = (ProductService) this.productManager.getIProduct(args[1]);
        return  ticket.tryToAdd(service);
    }

}
