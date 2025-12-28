package upm.tickets;

import upm.CLI;
import upm.Products.IProduct;
import upm.Products.Product;
import upm.Products.ProductService;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TicketFormatter {
    private static final DateTimeFormatter TICKET_ID_FORMAT = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#.0######");

    public static String ticketIDFormatter(boolean isTicketIDAutoGen, String ticketID, LocalDateTime fechaApertura){
        return isTicketIDAutoGen ? fechaApertura.format(TICKET_ID_FORMAT) + "-" + ticketID : ticketID;
    }

    public void printTicketsByCash(TicketManager ticketManager, String cashID){
        List<Ticket<?>> list = ticketManager.getTicketByCashId(cashID);
        if (list != null && !list.isEmpty()) {
            list.sort(Comparator.comparing(t -> t.getTicketMetadata().getTicketID()));
            System.out.println("Tickets:");
            for (Ticket<?> t : list) {
                System.out.println(t.getTicketMetadata().getTicketID() + "->" + t.getEstado());
            }
        }
    }

    public void printListTickets(TicketManager ticketManager){
        System.out.println("Ticket list : ");
        Map<String,Ticket<?>> ticketsByID = ticketManager.getTicketsById();
        List<Ticket<?>> ticketList = new ArrayList<>(ticketsByID.values());
        ticketList.sort(Comparator.comparing(t -> t.getTicketMetadata().getTicketID()));
        for (Ticket<?> t : ticketList) {
            System.out.println("  " + t.getTicketMetadata().getTicketID() + " - " + t.getEstado());
        }
    }

    public void printCurrentTicket(Ticket<? extends IProduct> ticket){
        ServiceProdDiscountCalc serProdCalc = new ServiceProdDiscountCalc();
        CategoryDiscountCalc catCalc = new CategoryDiscountCalc();

        DiscountResult discountResultCat = catCalc.calculateTotals(ticket);
        DiscountResult discountResultSerProd = serProdCalc.calculateTotals(ticket);
        char ticketType = whatTypeIsTheticket(ticket);
        if(ticketType == 'c'){
            //aquí decidimos si tiene servicios al ser ticket combinado calcular el nuevo total de precios para poner en el print


        } else if (ticketType == 's') {

        } else{
            //ticketType es p
            List<? extends IProduct> itemList = ticket.getItemsList();
            Map<Product, Double> hasDiscount = catCalc.discountPerProduct(ticket);
            if (!itemList.isEmpty()) {
                // Ordenamos los productos por nombre antes de imprimirlos
                ticket.sortProducts();
                System.out.println("Ticket : " + ticket.getTicketMetadata().getTicketID());
                for (IProduct currentItem : itemList) {
                    if(currentItem instanceof Product){
                        Product product = (Product) currentItem; //confirmamos a java que es un product y lo usamos
                        CLI.printText(currentItem.toString());
                        // Si el descuento no es igual a 1.0, el producto tiene descuento
                        boolean hasAnyDiscount = (hasDiscount.get(product)!=1.0);
                        if (hasAnyDiscount) {
                            //Si el discount es 1.0, significa que no tiene
                            double priceAfterDiscount = product.getPrice() * hasDiscount.get(product);
                            double discountAmount = product.getPrice() - priceAfterDiscount;
                            System.out.printf(" **Discount -%s%n", PRICE_FORMAT.format(discountAmount));
                        } else {
                            System.out.println(); //Aplicamos salto de linea si no hay descuento
                        }
                    }
                }
            }
            //Segun sale en el formato, el formato es US, el punto es el que marca el decimal.
            System.out.printf("\tTotal price: %s%n", PRICE_FORMAT.format(discountResultCat.getTotalWithout()));
            System.out.printf("\tTotal discount: %s%n", PRICE_FORMAT.format(discountResultCat.getTotalDiscount()));
            System.out.printf("\tFinal price: %s%n", PRICE_FORMAT.format(discountResultCat.getTotalWith()));
        }

    }

    private char whatTypeIsTheticket(Ticket<? extends IProduct> ticket){
        char type;
        if(ticket.getTicketType()==TicketType.SERVICE){
            type = 's';
        } else if (ticket.getTicketType()==TicketType.PRODUCT) {
            type = 'p';
        } else{
            type = 'c';
        }
        return type;
    }


    public void printFinalTicket(Ticket<? extends Product> ticket){
        printCurrentTicket(ticket);
    }

    public static String ticketIDFinalFormat(Ticket<? extends Product> ticket, LocalDateTime closingTime){
         return ticket.getTicketMetadata().getTicketID() + "-"
               + closingTime.format(TICKET_ID_FORMAT);
    }

}
