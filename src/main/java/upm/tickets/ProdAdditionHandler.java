package upm.tickets;
import upm.CLI;
import upm.Products.IProduct;
import java.util.List;

public abstract class ProdAdditionHandler<T extends IProduct>{

    /**
     * Se encargará de determinar si se puede gestionar el producto a poner en el
     * ticket con dicho metodo.
     */
    public boolean canHandle(IProduct product) {
        return product instanceof IProduct;
    }

    /**
     * Metodo que se encarga de averiguar si se puede poner dicho producto en el ticket
     * gestionando los errores especificos de cada especializacion
     */
    public boolean canBeAdded(Ticket<? super T> ticket, T product, List<String> customTexts){
        return true;
    }

    /**
     * Lógica común para insertar N veces hasta que se llene el ticket.
     * Devuelve true si se pudo añadir AL MENOS uno.
     */
    protected boolean addMultipleTimes(Ticket<? super T> ticket, T product, int quantity) {
        boolean prodAdded = true;
        boolean addedOnce = false;

        for (int i = 0; i < quantity && prodAdded; i++) {
            prodAdded = ticket.addProductToTicket(product);
            addedOnce = addedOnce || prodAdded;
        }

        if (!prodAdded) {
            CLI.print("You can't add more products to the ticket. Try to make a new one if needed.");
        }
        return addedOnce;
    }
}
