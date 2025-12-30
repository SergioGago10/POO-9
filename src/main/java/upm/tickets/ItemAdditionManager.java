package upm.tickets;

import upm.Products.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemAdditionManager {
    private final List<ItemAdditionHandler<?>> handlers = new ArrayList<>();

    public ItemAdditionManager() {
        handlers.add(new AddEventProduct());
        handlers.add(new AddCustomProduct());
        handlers.add(new AddBasicProduct());
        handlers.add(new AddServiceProduct());
    }

    public boolean process(Ticket<Item> ticket, Item product, int quantity, List<String> texts) {
        for (ItemAdditionHandler<?> handler : handlers) {
            if (handler.canHandle(product)) {
                //Hacemos cast para que el handler sea del producto específico que ha sido seleccionado
                ItemAdditionHandler<Item> castedHandler = (ItemAdditionHandler<Item>) handler;

                // Intentamos añadirlo las veces que quantity pida
                // y gestionará sus propios errores lógicos (poner más prods de los que se puedan meter, ...)
                if(castedHandler.canBeAdded(ticket, product, texts)) return castedHandler.addMultipleTimes(ticket,product,quantity);
            }
        }
        // Si nadie dio true en canHandle, no existe el comando para este producto
        return false;
    }
}
