package upm.tickets;

import upm.CLI;
import upm.Products.Item;
import upm.Products.ProductManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemAdditionManager {
    private final List<ItemAdditionHandler<?>> handlers = new ArrayList<>();

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
    public<T extends Item> boolean process(String[] args) {
      try{
          ProductManager productManager = ProductManager.getInstance();
          for (ItemAdditionHandler<?> handler : handlers) {
              if (handler.canHandle(productManager.getIProduct(args[1]))) {
                  //Hacemos cast para que el handler sea del producto específico que ha sido seleccionado
                  ItemAdditionHandler<T> castedHandler = (ItemAdditionHandler<T>) handler;

                  // Intentamos añadirlo las veces que quantity pida
                  // y gestionará sus propios errores lógicos (poner más prods de los que se puedan meter, ...)
                  if(castedHandler.canBeAdded(args)) return castedHandler.addMultipleTimes(args);
              }
          }
          // Si nadie dio true en canHandle, no existe el comando para este producto
          return false;
      } catch (ClassCastException e){
          //Estamos metiendo un producto a un ticket que no acepta ese tipo de producto, por lo que el cast falla
        return false;
      }
    }

}
