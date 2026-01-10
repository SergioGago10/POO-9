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
    public boolean canBeAdded(String[] args) {
        return true;
        //No hay restricciones de si se puede meter un basicProduct
        //Las únicas que hay se manejan en otros lugares ya
    }
    
    @Override
    protected boolean addMultipleTimes(String[] args) {
        return super.addMultipleTimes(args);
    }
    
}
