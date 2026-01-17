package upm.tickets.itemsaddition;
import upm.CLI;
import upm.products.*;
import upm.tickets.core.Ticket;

import java.time.LocalDateTime;

public class AddEventProduct extends ItemAdditionStrategy<Event> {

    @Override
    public boolean canHandle(Item product){
        return product instanceof Event;
    }

    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    public boolean canBeAdded(String[] args) {
        Event eventProd = (Event) this.productManager.getIProduct(args[1]);
        if(doesThisProdExistinTicket(this.ticketManager.getTicketById(args[0]),eventProd)){
            CLI.printErrorNextLine("Error -> This product (Food/Meeting) is already in the ticket. It can not be added again.");
            return false;
        }
        return isDateValid(eventProd);
    }

    private boolean doesThisProdExistinTicket(Ticket<?>ticket, Item product){
        return ticket.getItemsList().contains(product);
    }

    private boolean isDateValid(Event product){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime planned = product.getPlannedDate();
        if (product.getTypeEvent().equals(TypeEvent.FOOD)) {
            if (planned.isBefore(now.plusDays(3))) {
                CLI.printErrorNextLine("Error -> Foods must be planned at least 3 days before.");
                return false;
            }
        } else {
            if (planned.isBefore(now.plusHours(12))) {
                CLI.printErrorNextLine("Error -> Meetings must be planned at least 12 hours before.");
                return false;
            }
        }
        return true;
    }

    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    protected boolean addMultipleTimes(String[] args) {
        Ticket<?> ticket = this.ticketManager.getTicketById(args[0]);
        Event product = (Event) this.productManager.getIProduct(args[1]);
        int quantity = Integer.parseInt(args[2]);
        if(!isTheAmountValid(quantity,product)){
            CLI.printErrorNextLine("Error -> The amount of people that will attend the event exceeds the limit.");
            return  false;
        }
        boolean isDateValid = product.getPlannedDate().isAfter(LocalDateTime.now());
        if(!isDateValid){
            CLI.printErrorNextLine("Error -> You can't add an event that has expired.");
         return false;
        }

        double actualPrice = product.getPrice()*quantity;
        Event eventAux = new Event(product.getId(), product.getName(), actualPrice, product.getCreationDate(),
                product.getPlannedDate(), product.getMaxParticipants(), product.getTypeEvent(), quantity);

        boolean wasTheEventAdded = ticket.tryToAdd(eventAux);
        if (!wasTheEventAdded) {
            CLI.printErrorNextLine("Error -> The event could not be added, the max number of products has been reached.");
        }
        return wasTheEventAdded;
    }

    private boolean isTheAmountValid(int amount, Event product){
        return amount <= product.getMaxParticipants();
    }

}
