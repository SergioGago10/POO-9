package upm.tickets.items.addition;
import upm.CLI;
import upm.Products.Item;
import upm.Products.ProductManager;
import upm.tickets.core.Ticket;
import upm.tickets.management.TicketManager;

public abstract class ItemAdditionStrategy<T extends Item>{
    protected TicketManager ticketManager = TicketManager.getInstance();
    protected ProductManager productManager = ProductManager.getInstance();

    /**
     * Se encargará de determinar si se puede gestionar el producto a poner en el
     * ticket con dicho metodo.
     */
    public abstract boolean canHandle(Item product);

    /**
     * Metodo que se encarga de averiguar si se puede poner dicho producto en el ticket
     * gestionando los errores especificos de cada especializacion
     *
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    public abstract boolean canBeAdded(String[] args);


    /**
     * Lógica común para insertar N veces hasta que se llene el ticket.
     * Devuelve true si se pudo añadir AL MENOS uno.
     *
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    protected boolean addMultipleTimes(String[] args) {
        Ticket<?> ticket = ticketManager.getTicketById(args[0]);
        Item item =  productManager.getIProduct(args[1]);
        int quantity = Integer.parseInt(args[2]);
        boolean canContinueAdding = true;
        boolean addedOnce = false;

        for (int i = 0; i < quantity && canContinueAdding; i++) {
            canContinueAdding = ticket.tryToAdd(item);
            if (canContinueAdding) {
                addedOnce = true;
            }
        }

        if (ticket.getItemsList().size() >= ticket.getTicketMetadata().getMAX_PRODS_IN_TICKET()) {
            CLI.print("You can't add more products to the ticket. Try to make a new one if needed.");
        }
        return addedOnce;
    }
}
