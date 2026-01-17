package upm.tickets.itemsaddition;
import upm.products.*;

public abstract class ItemAdditionStrategy implements ItemAdditionVisitor{
        @Override
        public boolean add(BasicProduct p, String[] a) {
            return false;
        }
        @Override
        public boolean add(CustomizableProduct p, String[] a) {
            return false;
        }
        @Override
        public boolean add(Event p, String[] a) {
            return false;
        }
        @Override
        public boolean add(ProductService p, String[] a) {
            return false;
        }
}
