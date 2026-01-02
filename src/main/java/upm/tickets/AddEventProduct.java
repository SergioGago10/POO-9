package upm.tickets;
import upm.CLI;
import upm.Products.*;

import java.time.LocalDateTime;
import java.util.List;

public class AddEventProduct extends ItemAdditionHandler<Event> {
    private Event eventAux;

    @Override
    public boolean canHandle(Item product){
        return product instanceof Event;
    }

    @Override
    public boolean canBeAdded(Ticket<? super Event> ticket, Event product, List<String> customTexts) {
        if(ticket.getTicketType()==TicketType.SERVICE){
            return false;
        }
        if(doesThisProdExistinTicket(ticket,product)){
            CLI.print("This product (Food/Meeting) is already in the ticket. It can not be added again.");
            return false;
        }
        if(!isDateValid(product)){
            return false;
        }
        return true;
    }

    private boolean doesThisProdExistinTicket(Ticket<?>ticket, Item product){
        return ticket.getItemsList().contains(product);
    }

    private boolean isDateValid(Event product){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime planned = product.getPlannedDate();
        if (product.getTypeEvent().equals(TypeEvent.FOOD)) {
            if (planned.isBefore(now.plusDays(3))) {
                CLI.print("Foods must be planned at least 3 days before.");
                return false;
            }
        } else {
            if (planned.isBefore(now.plusHours(12))) {
                CLI.print("Meetings must be planned at least 12 hours before.");
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean addMultipleTimes(Ticket<? super Event> ticket, Event product, int quantity) {
        if(!isTheAmountValid(quantity,product)){
            CLI.print("The amount of people that will attend the event exceeds the limit.");
            return  false;
        }

        double actualPrice = product.getPrice()*quantity;
        this.eventAux = new Event(product.getId(), product.getName(), actualPrice,product.getCreationDate(),
                                  product.getPlannedDate(), product.getMaxParticipants(),product.getTypeEvent(), quantity);

        boolean wasTheEventAdded = ticket.addProductToTicket(this.eventAux);
        if (!wasTheEventAdded) {
            CLI.print("The event could not be added, the max number of products has been reached.");
        }
        return wasTheEventAdded;
    }

    private boolean isTheAmountValid(int amount, Event product){
        return amount <= product.getMaxParticipants();
    }

}
