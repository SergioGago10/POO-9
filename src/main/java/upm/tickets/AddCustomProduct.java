package upm.tickets;

import upm.CLI;
import upm.Products.CustomizableProduct;
import upm.Products.Item;

import java.util.ArrayList;
import java.util.List;

public class AddCustomProduct extends ItemAdditionHandler<CustomizableProduct> {
    private CustomizableProduct prodCustAux;

    @Override
    public boolean canHandle(Item product){
        return product instanceof CustomizableProduct;
    }

    @Override
    public boolean canBeAdded(Ticket<? super CustomizableProduct> ticket, CustomizableProduct product, List<String> customTexts) {
        if(ticket.getTicketType()==TicketType.SERVICE){
            return false;
        }
        List<String> textsToAdd = (customTexts == null) ? new ArrayList<>() : new ArrayList<>(customTexts);
        if(textsToAdd.size() > product.getMaxCustomTexts()){
            CLI.print("Too many custom texts for this product. Max allowed: " + product.getMaxCustomTexts());
            return false;
        }
        //Creamos una copia del producto original, ya que no queremos modificarlo y asignarlo con personalizaciones.
        this.prodCustAux = new CustomizableProduct(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getMaxCustomTexts()
        );
        this.prodCustAux.setCustomTexts(textsToAdd);
        double finalPrice = this.prodCustAux.calculateFinalPrice();
        this.prodCustAux.setPrice(finalPrice);
        return true;
    }

    @Override
    protected boolean addMultipleTimes(Ticket<? super CustomizableProduct> ticket, CustomizableProduct product, int quantity) {
        return super.addMultipleTimes(ticket, this.prodCustAux, quantity);
    }
    
}
