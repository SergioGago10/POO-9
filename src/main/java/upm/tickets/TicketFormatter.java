package upm.tickets;

import upm.CLI;
import upm.Products.*;
import upm.Users.Cash;
import upm.Users.UserManager;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketFormatter {
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

    public<T extends Item> void printCurrentTicket(Ticket<T> ticket){
        ServiceProdDiscountCalc serProdCalc = new ServiceProdDiscountCalc();
        CategoryDiscountCalc catCalc = new CategoryDiscountCalc();
        boolean areProductsInTicket = false, areServicesInTicket = false;
        List<T> itemsList = ticket.getItemsList();

        if (itemsList.isEmpty()) {
            //la lista está vacía, imprimimos solo el precio siendo este 0.0 y ya
            //da igual el descuento que apliquemos, sera siempre 0 ya que no hay nada
            DiscountResult discountInEmptyList = catCalc.calculateTotals(ticket);
            printPriceValues(discountInEmptyList);
            return;
        }

        for(T item : itemsList){
            if(item instanceof Product) areProductsInTicket = true;
            if(item instanceof ProductService) areServicesInTicket = true;
        }

        if(areProductsInTicket && areServicesInTicket){
            DiscountResult discountResultCat = catCalc.calculateTotals(ticket);
            DiscountResult discountResultSerProd = serProdCalc.calculateTotals(ticket);
            Map<Product, Double> hasDiscount = catCalc.discountPerProduct(ticket);
            List<ProductService> serviceList = ticket.getServicesSortedById();
            List<Product> prodList = ticket.getProductsSortedByName();
            CLI.print("Ticket: " + ticket.getTicketMetadata().getTicketID());
            if(!serviceList.isEmpty()){
                printServices(serviceList);
            }
            if(!prodList.isEmpty()){
                printProducts(prodList, hasDiscount);
            }
            printPriceValues(discountResultCat,discountResultSerProd);
        } else if (areServicesInTicket){
            List<ProductService> serviceList = ticket.getServicesSortedById();
            CLI.print("Ticket: " + ticket.getTicketMetadata().getTicketID());
            printServices(serviceList);
        } else {
            DiscountResult discountResultCat = catCalc.calculateTotals(ticket);
            Map<Product, Double> hasDiscount = catCalc.discountPerProduct(ticket);
            List<Product> productList = ticket.getProductsSortedByName();
            CLI.print("Ticket : " + ticket.getTicketMetadata().getTicketID());
            printProducts(productList,hasDiscount);
            printPriceValues(discountResultCat);
        }
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
                CLI.printText(((Event) currentItem).toTicketString());
            } else{
                CLI.printText(currentItem.toString());
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
