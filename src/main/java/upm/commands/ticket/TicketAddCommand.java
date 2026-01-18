package upm.commands.ticket;

import upm.CLI;
import upm.commands.core.Command;
import upm.products.*;
import upm.tickets.format.TicketFormatter;
import upm.users.Cash;
import upm.users.UserManager;
import upm.tickets.core.Ticket;
import upm.tickets.core.TicketState;
import upm.tickets.itemsaddition.ItemAdditionManager;
import upm.tickets.management.TicketManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TicketAddCommand extends Command {

    public TicketAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 5) {
            CLI.printErrorNextLine("""
                    Error -> format must be:\s
                    ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]\s
                    ticket add <ticketId> <cashId> <eventId> <peopleInEvent>\s
                    ticket add <ticketId> <cashId> <serviceId>S\s""");
            return true;
        }

        try {
            String ticketId = args[2];
            String cashId = args[3];
            String itemId = args[4];
            String amount = null;

            ProductManager productManager = ProductManager.getInstance();
            TicketManager ticketManager = TicketManager.getInstance();

            if (!productManager.idExists(itemId)) {
                CLI.printErrorNextLine("Error -> itemId must be an id contained in the catalog. Type 'prod list' to see all the catalog.");
                return true;
            }

            Ticket<? extends Item> ticketAModificar = ticketManager.getTicketById(ticketId);

            if (ticketAModificar == null) {
                CLI.printErrorNextLine("Error -> Ticket with id: " + ticketId + " does not exist.");
                return true;
            }

            Cash cashUser = (Cash) UserManager.getInstance().getUserByID(cashId);
            if (cashUser == null) {
                CLI.printErrorNextLine("Error -> Cashier with id: " + cashId + " does not exist.");
                return true;
            }

            if (!containsTicketId(cashUser.getTickets(), ticketAModificar)) {
                CLI.printErrorNextLine("Error -> Ticket with id: " + ticketId + " does not belong to cashier " + cashId);
                return true;
            }

            if(ticketAModificar.getEstado() == TicketState.CLOSE) {
                CLI.printErrorNextLine("Error -> Ticket with id: " + ticketId + " is closed, and no products can be added to it.");
                return true;
            }

            if(args.length > 5) {
                amount = args[5];
            }

            String[] texts = null;
            if(args.length > 6) {
                texts = parseCustomizations(Arrays.copyOfRange(args,6,args.length));
                if(texts == null) return true;
            }

            List<String> argsDT = new ArrayList<>();
            argsDT.add(ticketId);
            argsDT.add(itemId);
            if(amount != null) {
                argsDT.add(amount);
            }
            if(texts != null) {
                argsDT.addAll(Arrays.asList(texts));
            }
            String[] argsDTO = argsDT.toArray(new String[0]);

            ItemAdditionManager additionManager = new ItemAdditionManager();
            Item item = productManager.getIProduct(itemId);
            boolean handled = additionManager.process(argsDTO, item);

            boolean isTicketFull = ticketAModificar.getItemsList().size()
                    >= ticketAModificar.getTicketMetadata().getMAX_PRODS_IN_TICKET();

            if(!handled && !isTicketFull){
                CLI.printErrorNextLine("Error -> Product with id " + itemId + " could not be added, " +
                        "it may be an invalid type or " +
                        "it simply couldn't be added, " +
                        "(remember that you can not add services to " +
                        "an only product ticket and viceversa).");
                return true;
            }

            if (!ticketAModificar.getItemsList().isEmpty() && ticketAModificar.getEstado() == TicketState.EMPTY) {
                ticketAModificar.setEstado(TicketState.OPEN);
            }

            if(handled){
                TicketFormatter ticketFormatter = new TicketFormatter();
                ticketFormatter.printCurrentTicket(ticketAModificar);
                CLI.printNextLine("ticket add: ok");
            }
        } catch (NumberFormatException e) {
            CLI.printErrorNextLine("Error -> amount must be an integer.");
        } catch (Exception e) {
            CLI.printErrorNextLine("Error -> product could not be added to ticket: " + e.getMessage());
        }

        return true;
    }

    private String[] parseCustomizations(String[] args) {
        ArrayList<String> customizations = new ArrayList<>();
        boolean correctFormat = true;

        for (int i = 0; i < args.length && correctFormat; i++) {
            String s = args[i];
            if (!s.startsWith("--p")) {
                CLI.printErrorNextLine("Error -> format expected --p<txt>, found: " + s);
                correctFormat = false;
            }
            if (correctFormat) {
                customizations.add(s.substring(3));
            }
        }

        return correctFormat ? customizations.toArray(new String[0]) : null;
    }

    private boolean containsTicketId(List<?> tickets, Ticket<?> target) {
        String targetId = safeTicketId(target);
        if (targetId == null) return false;
        if (tickets == null) return false;

        for (Object o : tickets) {
            if (o instanceof Ticket<?> t) {
                if (Objects.equals(safeTicketId(t), targetId)) return true;
            }
        }
        return false;
    }

    private String safeTicketId(Ticket<?> t) {
        if (t == null) return null;
        if (t.getTicketMetadata() == null) return null;
        return t.getTicketMetadata().getTicketID();
    }
}
