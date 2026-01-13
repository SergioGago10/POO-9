package upm.tickets.items.addition;

import upm.products.ProductManager;

import java.util.ArrayList;
import java.util.List;

public class ItemAdditionManager {
    private final List<ItemAdditionStrategy<?>> handlers = new ArrayList<>();

    public ItemAdditionManager() {
        handlers.add(new AddEventProduct());
        handlers.add(new AddCustomProduct());
        handlers.add(new AddBasicProduct());
        handlers.add(new AddServiceProduct());
    }

    /**
     *
     * @param args [ticketId, itemId, amount, texts(if they had)]
     */
    public boolean process(String[] args) {
      try{
          ProductManager productManager = ProductManager.getInstance();
          for (ItemAdditionStrategy<?> handler : handlers) {
              if (handler.canHandle(productManager.getIProduct(args[1]))) {
                  // Intentamos añadirlo las veces que quantity pida
                  // y gestionará sus propios errores lógicos (poner más prods de los que se puedan meter, ...)
                  if(handler.canBeAdded(args)) return handler.addMultipleTimes(args);
              }
          }
          // Si nadie dio true en canHandle, no existe el comando para este producto
          return false;
      } catch (Exception e){
          //En caso de algun fallo, daremos false porque no se pudo meter el producto al ticket
        return false;
      }
    }

}
