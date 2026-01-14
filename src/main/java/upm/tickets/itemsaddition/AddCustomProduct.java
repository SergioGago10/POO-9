package upm.tickets.itemsaddition;

import upm.CLI;
import upm.products.CustomizableProduct;
import upm.products.Item;
import upm.tickets.core.Ticket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCustomProduct extends ItemAdditionStrategy<CustomizableProduct> {
    private CustomizableProduct prodCustAux;

    @Override
    public boolean canHandle(Item product){
        return product instanceof CustomizableProduct;
    }

    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    public boolean canBeAdded(String[] args) {
        CustomizableProduct product = (CustomizableProduct) this.productManager.getIProduct(args[1]);
        List<String> textsToAdd = null;
        if(args.length > 3){
            textsToAdd = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 3, args.length)));
        }
        if(textsToAdd != null && textsToAdd.size() > product.getMaxCustomTexts()){
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

    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    protected boolean addMultipleTimes(String[] args) {
        Ticket<?> ticket = this.ticketManager.getTicketById(args[0]);
        int quantity = Integer.parseInt(args[2]);
        boolean canAddMore = true;
        boolean addedAtLeastOne = false;

        for (int i = 0; i < quantity && canAddMore; i++) {
            canAddMore = ticket.tryToAdd(this.prodCustAux);

            if (canAddMore) {
                addedAtLeastOne = true;
            }
        }

        if (ticket.getItemsList().size() >= ticket.getTicketMetadata().getMAX_PRODS_IN_TICKET()) {
            CLI.print("You can't add more products to the ticket. Try to make a new one if needed.");
        }
        return addedAtLeastOne;
    }
    
}
