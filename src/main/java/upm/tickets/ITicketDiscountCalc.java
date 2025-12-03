package upm.tickets;

import upm.Products.Product;
import java.util.Map;

public interface ITicketDiscountCalc{
    //Interfaz que usaremos para aplicar cualquier "estrategia" de descuentos, ya que en un futuro pueden cambiar
    Map<Product, Double> discountPerProduct(Ticket ticket);
    double[] calculateTotals(Ticket ticket); // Devuelve: [precio sin descuento, precio con descuento, total descuento]
}
