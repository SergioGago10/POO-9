package upm.tickets;

import upm.CLI;
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

    public<T extends Item> boolean process(Ticket<T> ticket, T product, int quantity, List<String> texts) {
      try{
          for (ItemAdditionHandler<?> handler : handlers) {
              if (handler.canHandle(product)) {
                  //Hacemos cast para que el handler sea del producto específico que ha sido seleccionado
                  ItemAdditionHandler<T> castedHandler = (ItemAdditionHandler<T>) handler;

                  // Intentamos añadirlo las veces que quantity pida
                  // y gestionará sus propios errores lógicos (poner más prods de los que se puedan meter, ...)
                  if(castedHandler.canBeAdded(ticket, product, texts)) return castedHandler.addMultipleTimes(ticket,product,quantity);
              }
          }
          // Si nadie dio true en canHandle, no existe el comando para este producto
          return false;
      } catch (ClassCastException e){
          //Estamos metiendo un producto a un ticket que no acepta ese tipo de producto, por lo que el cast falla
          CLI.print("A product of this class: "+product.getClass()+" can not be added to this type of ticket.");
        return false;
      }
    }

}
