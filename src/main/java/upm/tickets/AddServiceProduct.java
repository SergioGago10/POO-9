package upm.tickets;

import upm.CLI;
import upm.Products.CustomizableProduct;
import upm.Products.Item;
import upm.Products.ProductService;

import java.time.LocalDateTime;
import java.util.List;

public class AddServiceProduct extends ItemAdditionHandler<ProductService>{

    @Override
    public boolean canHandle(Item product){
        return product instanceof ProductService;
    }

    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    public boolean canBeAdded(String[] args) {
        Ticket<ProductService> ticket = (Ticket<ProductService>) this.ticketManager.getTicketById(args[0]);
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
        Ticket<ProductService> ticket =  (Ticket<ProductService>) this.ticketManager.getTicketById(args[0]);
        ProductService service = (ProductService) this.productManager.getIProduct(args[1]);
        return  ticket.addProductToTicket(service);
    }

}
