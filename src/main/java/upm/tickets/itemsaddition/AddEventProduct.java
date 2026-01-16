package upm.tickets.itemsaddition;
import upm.CLI;
import upm.products.*;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;

import java.time.LocalDateTime;

public class AddEventProduct extends ItemAdditionStrategy {
    private TicketManager ticketManager = TicketManager.getInstance();

    /**
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    @Override
    public boolean add(Event event, String[] args) {
        if(doesThisProdExistinTicket(this.ticketManager.getTicketById(args[0]),event)){
            CLI.printErrorNextLine("Error -> This product (Food/Meeting) is already in the ticket. It can not be added again.");
            return false;
        }
        if(!isDateValid(event)){
            return false;
        }

        Ticket<?> ticket = this.ticketManager.getTicketById(args[0]);
        int quantity = Integer.parseInt(args[2]);
        if(!isTheAmountValid(quantity,event)){
            CLI.printErrorNextLine("Error -> The amount of people that will attend the event exceeds the limit.");
            return  false;
        }
        boolean isDateValid = event.getPlannedDate().isAfter(LocalDateTime.now());
        if(!isDateValid){
            CLI.printErrorNextLine("Error -> You can't add an event that has expired.");
            return false;
        }

        double actualPrice = event.getPrice()*quantity;
        Event eventAux = new Event(event.getId(), event.getName(), actualPrice, event.getCreationDate(),
                event.getPlannedDate(), event.getMaxParticipants(), event.getTypeEvent(), quantity);

        boolean wasTheEventAdded = ticket.tryToAdd(eventAux);
        if (!wasTheEventAdded) {
            CLI.printErrorNextLine("Error -> The event could not be added, the max number of products has been reached.");
        }
        return wasTheEventAdded;
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

    private boolean isTheAmountValid(int amount, Event product){
        return amount <= product.getMaxParticipants();
    }

}
