package upm.tickets.core;

/**
 * Esta clase se utiliza para aplicar el patron Visitor - Double Dispatcher
 *
 */
public interface TicketRenderer {
    void renderPrices(ProductTicket ticket);
    void renderPrices(ServiceTicket ticket);
    void renderPrices(CommonTicket ticket);
}