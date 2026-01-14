package upm.tickets.format;

import upm.CLI;
import upm.products.*;
import upm.users.Cash;
import upm.users.UserManager;
import upm.tickets.core.*;
import upm.tickets.discount.CategoryDiscountCalc;
import upm.tickets.discount.DiscountResult;
import upm.tickets.discount.ServiceProdDiscountCalc;
import upm.tickets.management.TicketManager;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketFormatter implements TicketRenderer {
    private static final DateTimeFormatter TICKET_ID_FORMAT = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("0.0######");

    public void printTicketsByCash(String cashID){
        Cash cashUser = (Cash) UserManager.getInstance().getUserByID(cashID);
        List<Ticket<?>> ticketsByCashId = cashUser.getTickets();
        if (!ticketsByCashId.isEmpty()) {
            ticketsByCashId.sort(Comparator.comparing(t -> t.getTicketMetadata().getTicketID()));
            System.out.println("Tickets:");
            for (Ticket<?> t : ticketsByCashId) {
                System.out.println(t.getTicketMetadata().getTicketID() + "->" + t.getEstado());
            }
        }
    }

    public void printListTickets(TicketManager ticketManager){
        System.out.println("Ticket list : ");
        List<Ticket<?>> ticketsList = ticketManager.getTicketsList();
        ticketsList.sort(Comparator.comparing((Ticket<?> t) -> t.getTicketMetadata().getTicketID()).reversed());
        for (Ticket<?> t : ticketsList) {
            System.out.println("  " + t.getTicketMetadata().getTicketID() + " - " + t.getEstado());
        }
    }

    public void printCurrentTicket(Ticket<?> ticket){
        CategoryDiscountCalc catCalc = new CategoryDiscountCalc();

        //Obtenemos el contenido del ticket ya filtrado y sorted
        TicketContent content = ticket.getSortedContent();

        Map<Product, Double> hasDiscount = catCalc.discountPerProduct(ticket);


        CLI.print("Ticket: " + ticket.getTicketMetadata().getTicketID());

        if (!content.getServices().isEmpty()) {
            printServices(content.getServices());
        }

        if (!content.getProducts().isEmpty()) {
            printProducts(content.getProducts(), hasDiscount);
        }


        //Aplicamos el double-dispatcher
        ticket.accept(this);
    }

    //Esta tecnica se llama double dispatcher, es un patron de dise;o
    @Override
    public void renderPrices(ProductTicket ticket) {
        DiscountResult res = new CategoryDiscountCalc().calculateTotals(ticket);
        printPriceValues(res); // El formato simple
    }

    @Override
    public void renderPrices(CommonTicket ticket) {
        DiscountResult resCat = new CategoryDiscountCalc().calculateTotals(ticket);
        DiscountResult resSer = new ServiceProdDiscountCalc().calculateTotals(ticket);
        printPriceValues(resCat, resSer); // El formato mixto
    }

    @Override
    public void renderPrices(ServiceTicket ticket) {
        // Si el ticket es de servicios no se imprime nada
    }

    private void printServices(List<ProductService> serviceList){
        CLI.print("Services included:");
        for(ProductService item : serviceList){
            CLI.print(item.toString());
        }
    }

    private void printProducts(List<Product> productList, Map<Product, Double> hasDiscount){
        CLI.print("Product included:");
        for (Product currentItem : productList) {
            if(currentItem instanceof Event){
                CLI.print(((Event) currentItem).toTicketString());
            } else{
                CLI.print(currentItem.toString());
            }
            // Si el descuento no es igual a 1.0, el producto tiene descuento
            boolean hasAnyDiscount = (hasDiscount.get(currentItem)!=1.0);
            if (hasAnyDiscount) {
                //Si el discount es 1.0, significa que no tiene
                double priceAfterDiscount = currentItem.getPrice() * hasDiscount.get(currentItem);
                double discountAmount = currentItem.getPrice() - priceAfterDiscount;
                System.out.printf(" **Discount -%s%n", PRICE_FORMAT.format(discountAmount));
            } else {
                //Aplicamos un salto de linea para el foramto ya que si no tiene descuento, no lo hace
                System.out.println();
            }
        }
    }

    //para prod
    private void printPriceValues(DiscountResult discountResult){
        System.out.printf("\tTotal price: %s%n", PRICE_FORMAT.format(discountResult.getTotalWithout()));
        System.out.printf("\tTotal discount: %s%n", PRICE_FORMAT.format(discountResult.getTotalDiscount()));
        System.out.printf("\tFinal price: %s%n", PRICE_FORMAT.format(discountResult.getTotalWith()));
    }
    //para composite
    private void printPriceValues(DiscountResult discountResultCat, DiscountResult discountResultSerProd){
        //solo hay sentido en poner precio si hay productos.
        if(discountResultCat.getTotalWithout() != 0){
            double totalDiscount = Math.min(discountResultSerProd.getTotalDiscount() +
                                            discountResultCat.getTotalDiscount(),
                                            discountResultCat.getTotalWithout());
            System.out.printf("\tTotal price: %s%n", PRICE_FORMAT.format(discountResultCat.getTotalWithout()));
            System.out.printf("\tExtra Discount from services: %s **discount -%s%n",
                    PRICE_FORMAT.format(discountResultSerProd.getTotalDiscount()),
                    PRICE_FORMAT.format(discountResultSerProd.getTotalDiscount()));

            System.out.printf("\tTotal discount: %s%n", PRICE_FORMAT.format(totalDiscount));
            System.out.printf("\tFinal price: %s%n", PRICE_FORMAT.format(discountResultCat.getTotalWithout()
                                                                            - totalDiscount));
        }
    }

    public void printFinalTicket(Ticket<? extends Item> ticket){
        printCurrentTicket(ticket);
    }

    public static String ticketIDFinalFormat(Ticket<? extends Item> ticket, LocalDateTime closingTime){
         return ticket.getTicketMetadata().getTicketID() + "-"
               + closingTime.format(TICKET_ID_FORMAT);
    }

}
