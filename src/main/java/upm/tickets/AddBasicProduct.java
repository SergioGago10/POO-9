package upm.tickets;
import upm.Products.BasicProduct;
import upm.Products.Item;
import upm.Products.ProductService;

import java.util.List;

public class AddBasicProduct extends ItemAdditionHandler<BasicProduct> {

    @Override
    public boolean canHandle(Item product){
        return product instanceof BasicProduct;
    }

    @Override
    public boolean canBeAdded(String[] args) {
        Ticket<?> ticket = this.ticketManager.getTicketById(args[0]);
        return !ticket.getTicketMetadata().getClassType().equals(ProductService.class);
    }
    
    @Override
    protected boolean addMultipleTimes(String[] args) {
        return super.addMultipleTimes(args);
    }
    
}
