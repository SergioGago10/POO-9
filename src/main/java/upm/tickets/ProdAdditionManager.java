package upm.tickets;

import upm.Products.IProduct;
import java.util.ArrayList;
import java.util.List;

public class ProdAdditionManager {
    private final List<ProdAdditionHandler<?>> handlers = new ArrayList<>();

    public ProdAdditionManager() {
        handlers.add(new AddEventProduct());
        handlers.add(new AddCustomProduct());
        handlers.add(new AddBasicProduct());
    }

    public boolean process(Ticket<IProduct> ticket, IProduct product, int quantity, List<String> texts) {
        for (ProdAdditionHandler<?> handler : handlers) {
            if (handler.canHandle(product)) {
                //Hacemos cast para que el handler sea del producto específico que ha sido seleccionado
                ProdAdditionHandler<IProduct> castedHandler = (ProdAdditionHandler<IProduct>) handler;

                // Intentamos añadirlo las veces que quantity pida
                // y gestionará sus propios errores lógicos (poner más prods de los que se puedan meter, ...)
                if(castedHandler.canBeAdded(ticket, product, texts)) return castedHandler.addMultipleTimes(ticket,product,quantity);
            }
        }
        // Si nadie dio true en canHandle, no existe el comando para este producto
        return false;
    }
}
