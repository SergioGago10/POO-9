package upm.tickets;
import upm.CLI;
import upm.Products.Item;
import upm.Products.ProductManager;

import java.util.Arrays;
import java.util.List;

public abstract class ItemAdditionHandler<T extends Item>{
    protected  TicketManager ticketManager = TicketManager.getInstance();
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
        Ticket<T> ticket = (Ticket<T>) ticketManager.getTicketById(args[0]);
        T item = (T) productManager.getIProduct(args[1]);
        int quantity = Integer.parseInt(args[2]);
        boolean prodAdded = true;
        boolean addedOnce = false;

        for (int i = 0; i < quantity && prodAdded; i++) {
            prodAdded = ticket.addProductToTicket(item);
            addedOnce = addedOnce || prodAdded;
        }

        if (!prodAdded) {
            CLI.print("You can't add more products to the ticket. Try to make a new one if needed.");
        }
        return addedOnce;
    }
}
