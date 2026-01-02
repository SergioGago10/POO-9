package upm.tickets;

import upm.CLI;
import upm.Products.Item;
import upm.Products.ProductService;

import java.time.LocalDateTime;
import java.util.List;

public class AddServiceProduct extends ItemAdditionHandler<ProductService>{

    @Override
    public boolean canHandle(Item product){
        return product instanceof ProductService;
    }

    @Override
    public boolean canBeAdded(Ticket<? super ProductService> ticket, ProductService service, List<String> customTexts) {
        if(ticket.getTicketType()==TicketType.PRODUCT){
            return false;
        }
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
    protected boolean addMultipleTimes(Ticket<? super ProductService> ticket, ProductService service, int quantity) {
        return  ticket.addProductToTicket(service);
    }

}
