package upm.tickets;

import upm.Products.Item;
import upm.Products.Product;
import upm.Products.ProductService;

public class ServiceProdDiscountCalc implements ITicketDiscountCalc {

    @Override
    public DiscountResult calculateTotals(Ticket<? extends Item> ticket) {
        int numberOfServices = 0;
        double totalWithout = 0.0;
        double totalWith;
        for (Item product : ticket.getItemsList()) {
            if (product instanceof ProductService) {
                numberOfServices++;
            } else{
                //al llegar aquí sabemos que es Product, ya que no puede ser servicio
                Product productAdder = (Product) product;
                double price = productAdder.getPrice();
                totalWithout += price;
            }
        }
        double percentagetToApply = 0.15 * numberOfServices;
        if(percentagetToApply >= 1) percentagetToApply = 1;
        //si es 1 o mayor a 1, lo dejamos en 1 y resultara en que todo sera gratis.
        double totalDiscount = percentagetToApply * totalWithout;
        totalWith = totalWithout - totalDiscount;
        return new DiscountResult(totalWithout,totalWith,totalDiscount);
    }
}
