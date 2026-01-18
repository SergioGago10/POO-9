package upm.tickets.itemsaddition;

import upm.products.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase utiliza el patron Chain of Responsibility
 * por si cabía alguna duda, ya que se menciona en el código
 * Visitor, aquí no se aplica visitor.
 */
public class ItemAdditionManager {
    private final List<ItemAdditionStrategy> handlers = new ArrayList<>();

    public ItemAdditionManager() {
        handlers.add(new AddEventProduct());
        handlers.add(new AddCustomProduct());
        handlers.add(new AddBasicProduct());
        handlers.add(new AddServiceProduct());
    }

    /**
     *
     * @param args [ticketId, itemId, amount(if they had), texts(if they had)]
     */
    public boolean process(String[] args, Item item) {
        if (item == null) return false;

        for (ItemAdditionStrategy handler : handlers) {
            // Usamos el patron Visitor/Double-Dispatcher.
            // Si el handler tiene el código para ese item, lo ejecuta y devuelve true.
            // Si no, devuelve false y seguimos al siguiente.
            if (item.accept(handler, args)) {
                return true;
            }
        }
        return false;
    }
}
