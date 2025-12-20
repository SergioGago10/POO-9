package upm.tickets;
import upm.CLI;
import upm.Products.*;

import java.time.LocalDateTime;
import java.util.List;

public class AddEventProduct extends ProdAdditionHandler<Event> {

    @Override
    public boolean canHandle(IProduct product){
        return product instanceof Event;
    }
    
    @Override
    public boolean canBeAdded(Ticket<? super Event> ticket, Event product, List<String> customTexts) {
        boolean prodAdded = true, addedOnce = false;
        if(doesThisProdExistinTicket(ticket,product)){
            CLI.print("This product (Food/Meeting) is already in the ticket. It can not be added again.");
            return false;
        }
        if(!isDateValid(product)){
            return false;
        }
        return true;
    }

    private boolean doesThisProdExistinTicket(Ticket<?>ticket,IProduct product){
        return ticket.getProductsList().contains(product);
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
        return super.addMultipleTimes(ticket, product, quantity);
    }
    
}
