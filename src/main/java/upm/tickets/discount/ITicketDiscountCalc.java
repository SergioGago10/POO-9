package upm.tickets.discount;

import upm.products.Item;
import upm.tickets.core.Ticket;

public interface ITicketDiscountCalc{
    //Interfaz que usaremos para aplicar cualquier "estrategia" de descuentos, ya que en un futuro pueden cambiar
    DiscountResult calculateTotals(Ticket<? extends Item> ticket); // Devuelve: [precio sin descuento, precio con descuento, total descuento]
}
