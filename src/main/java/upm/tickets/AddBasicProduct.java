package upm.tickets;
import upm.Products.BasicProduct;
import upm.Products.Item;

import java.util.List;

public class AddBasicProduct extends ItemAdditionHandler<BasicProduct> {

    @Override
    public boolean canHandle(Item product){
        return product instanceof BasicProduct;
    }

    @Override
    public boolean canBeAdded(Ticket<? super BasicProduct> ticket, BasicProduct product, List<String> customTexts) {
        //Un producto basico no tiene ningun tipo de restriccion, solo si pasa el límite de productos en la lista
        return super.canBeAdded(ticket, product, customTexts);
    }
    
    @Override
    protected boolean addMultipleTimes(Ticket<? super BasicProduct> ticket, BasicProduct product, int quantity) {
        return super.addMultipleTimes(ticket, product, quantity);
    }
    
}
