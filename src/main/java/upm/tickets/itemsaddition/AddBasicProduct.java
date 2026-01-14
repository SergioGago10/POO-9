package upm.tickets.itemsaddition;
import upm.products.BasicProduct;
import upm.products.Item;
public class AddBasicProduct extends ItemAdditionStrategy<BasicProduct> {

    @Override
    public boolean canHandle(Item product){
        return product instanceof BasicProduct;
    }

    @Override
    public boolean canBeAdded(String[] args) {
        return true;
    }
    
    @Override
    protected boolean addMultipleTimes(String[] args) {
        return super.addMultipleTimes(args);
    }
    
}
