package upm.tickets.itemsaddition;
import upm.products.BasicProduct;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;

public class AddBasicProduct extends ItemAdditionStrategy {
    private TicketManager ticketManager = TicketManager.getInstance();


    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    public boolean add(BasicProduct basic, String[] args) {
        Ticket<?> ticket = this.ticketManager.getTicketById(args[0]);
        int quantity = Integer.parseInt(args[2]);
        boolean canAddMore = true;
        boolean addedAtLeastOne = false;

        for (int i = 0; i < quantity && canAddMore; i++) {
            canAddMore = ticket.tryToAdd(basic);

            if (canAddMore) {
                addedAtLeastOne = true;
            }
        }
        return addedAtLeastOne;
    }
    
}
