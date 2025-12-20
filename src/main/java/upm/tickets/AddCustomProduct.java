package upm.tickets;

import upm.CLI;
import upm.Products.BasicProduct;
import upm.Products.CustomizableProduct;
import upm.Products.IProduct;

import java.util.ArrayList;
import java.util.List;

public class AddCustomProduct extends ProdAdditionHandler<CustomizableProduct> {

    @Override
    public boolean canHandle(IProduct product){
        return product instanceof CustomizableProduct;
    }

    @Override
    public boolean canBeAdded(Ticket<? super CustomizableProduct> ticket, CustomizableProduct product, List<String> customTexts) {
        List<String> textsToAdd = (customTexts == null) ? new ArrayList<>() : new ArrayList<>(customTexts);

        if(textsToAdd.size() > product.getMaxCustomTexts()){
            CLI.print("Too many custom texts for this product. Max allowed: " + product.getMaxCustomTexts());
            return false;
        }
        //Creamos una copia del producto original, ya que no queremos modificarlo y asignarlo con personalizaciones.
        CustomizableProduct copy = new CustomizableProduct(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getMaxCustomTexts()
        );
        copy.setCustomTexts(textsToAdd);
        double finalPrice = copy.calculateFinalPrice();
        copy.setPrice(finalPrice);
        return true;
    }

    @Override
    protected boolean addMultipleTimes(Ticket<? super CustomizableProduct> ticket, CustomizableProduct product, int quantity) {
        return super.addMultipleTimes(ticket, product, quantity);
    }
    
}
