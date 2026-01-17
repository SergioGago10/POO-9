package upm.tickets.itemsaddition;

import upm.products.BasicProduct;
import upm.products.CustomizableProduct;
import upm.products.Event;
import upm.products.ProductService;

public interface ItemAdditionVisitor {
    boolean add(BasicProduct product, String[] args);
    boolean add(CustomizableProduct product, String[] args);
    boolean add(Event product, String[] args);
    boolean add(ProductService product, String[] args);
}
