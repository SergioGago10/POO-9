package upm.tickets.items.addition;
import upm.Products.BasicProduct;
import upm.Products.Item;
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
