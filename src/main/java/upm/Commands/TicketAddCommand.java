package upm.Commands;

import upm.CLI;
import upm.Products.ProductManager;
import upm.tickets.ProdAdditionManager;
import upm.tickets.Ticket;
import upm.tickets.TicketManager;
import upm.tickets.TicketState;

import java.util.ArrayList;

public class TicketAddCommand extends Command {

    public TicketAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 6) {
            System.err.println("ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]");
            return true;
        }

        try {
            String ticketId = args[2];
            String cashId = args[3];
            String prodId = args[4];
            int amount = Integer.parseInt(args[5]);
            ProductManager productManager = ProductManager.getInstance();
            TicketManager ticketManager = TicketManager.getInstance();
            // Validación del producto
            if (!productManager.idExists(prodId)) {
                System.err.println("prodId must be an id contained in the catalog. Type 'prod list' to see all the catalog.");
                return true;
            }

            // Buscar ticket
            Ticket ticketAModificar = ticketManager.getTicketById(ticketId);

            if (ticketAModificar == null) {
                System.err.println("Error: Ticket " + ticketId + " does not exist.");
                return true;
            }

            // Comprobar que pertenece al mismo cashId
            if (!ticketAModificar.getTicketMetadata().getCashID().equals(cashId)) {
                System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                return true;
            }

            if(ticketAModificar.getEstado()==TicketState.CLOSE){
                System.err.println("Error: Ticket " + ticketId + " is closed, and no products can be added to it.");
                return true;
            }

            // Customizaciones
            ArrayList<String> customTexts = parseCustomizations(args);


            boolean prodAdded;

            ProdAdditionManager additionManager = new ProdAdditionManager();
            boolean handled = additionManager.process(ticketAModificar,productManager.getIProduct(prodId), amount, customTexts);

            if(!handled){
                System.err.println("Error: Product " + prodId + " has an unkown or invalid type, it can't be added.");
                return true;
            }
            if (!ticketAModificar.getProductsList().isEmpty() && ticketAModificar.getEstado() == TicketState.EMPTY) {
                //Ticket vacio que ahora no lo es, debe ser open y no empty
                ticketAModificar.setEstado(TicketState.OPEN);
            }
            ticketManager.getFormatter().printCurrentTicket(ticketAModificar);
            CLI.print("ticket add: ok");
        } catch (NumberFormatException e) {
            System.err.println("amount must be an integer.");
        } catch (Exception e) {
            System.err.println("Error adding product to ticket: " + e.getMessage());
        }

        return true;
    }

    /**
     *
     * @param args personalizaciones si es que las hay
     * @return null si no tiene personalizaciones o arraylist con las personalizaciones
     */
    private ArrayList<String> parseCustomizations(String[] args) {
        if (args.length <= 6) return null;

        ArrayList<String> customizations = new ArrayList<>();
        boolean correctFormat = true;

        for (int i = 6; i < args.length && correctFormat; i++) {
            String s = args[i];
            if (!s.startsWith("--p")) {
                System.err.println("Error: expected --p<txt>, found: " + s);
                correctFormat = false;
            }
            if (correctFormat) {
                customizations.add(s.substring(3));
            }
        }

        return correctFormat ? customizations : null;
    }
}
