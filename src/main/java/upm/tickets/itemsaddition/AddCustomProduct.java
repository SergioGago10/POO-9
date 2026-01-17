package upm.tickets.itemsaddition;

import upm.CLI;
import upm.products.CustomizableProduct;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCustomProduct extends ItemAdditionStrategy{
    private CustomizableProduct prodCustAux;
    private TicketManager ticketManager = TicketManager.getInstance();

    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    public boolean add(CustomizableProduct custom, String[] args) {
        List<String> textsToAdd = null;
        if(args.length > 3){
            textsToAdd = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 3, args.length)));
        }
        if(textsToAdd != null && textsToAdd.size() > custom.getMaxCustomTexts()){
            CLI.printErrorNextLine("Error -> Too many custom texts for this product. Max allowed: " + custom.getMaxCustomTexts());
            return false;
        }
        //Creamos una copia del producto original, ya que no queremos modificarlo y asignarlo con personalizaciones.
        this.prodCustAux = new CustomizableProduct(
                custom.getId(),
                custom.getName(),
                custom.getCategory(),
                custom.getPrice(),
                custom.getMaxCustomTexts()
        );
        this.prodCustAux.setCustomTexts(textsToAdd);
        double finalPrice = this.prodCustAux.calculateFinalPrice();
        this.prodCustAux.setPrice(finalPrice);

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
        return addedAtLeastOne;
    }
    
}
