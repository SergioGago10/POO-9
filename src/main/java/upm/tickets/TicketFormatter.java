package upm.tickets;

import upm.CLI;
import upm.Products.Product;

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
    private ITicketDiscountCalc discountCalc;

    public static String ticketIDFormatter(boolean isTicketIDAutoGen, String ticketID, LocalDateTime fechaApertura){
        return isTicketIDAutoGen ? fechaApertura.format(TICKET_ID_FORMAT) + "-" + ticketID : ticketID;
    }

    public void printTicketsByCash(TicketManager ticketManager, String cashID){
        List<Ticket> list = ticketManager.getTicketByCashId(cashID);
        if (list != null && !list.isEmpty()) {
            list.sort(Comparator.comparing(t -> t.getTicketMetadata().getTicketID()));
            System.out.println("Tickets:");
            for (Ticket t : list) {
                System.out.println(t.getTicketMetadata().getTicketID() + "->" + t.getEstado());
            }
        }
    }

    public void printListTickets(TicketManager ticketManager){
        System.out.println("Ticket list : ");
        Map<String,Ticket> ticketsByID = ticketManager.getTicketsById();
        List<Ticket> ticketList = new ArrayList<>(ticketsByID.values());
        ticketList.sort(Comparator.comparing(t -> t.getTicketMetadata().getTicketID()));
        for (Ticket t : ticketList) {
            System.out.println("  " + t.getTicketMetadata().getTicketID() + " - " + t.getEstado());
        }
    }

    public void printCurrentTicket(Ticket<? extends Product> ticket){
        this.discountCalc = new CategoryDiscountCalc(); //nuestra estrategia a aplicar es descuentos por categoria
        CategoryDiscountCalc catCalc = (CategoryDiscountCalc) discountCalc; // Hacemos cast para poder "separar" el objeto de la interfaz
        DiscountResult discountResult = catCalc.calculateTotals(ticket);
        List<? extends Product> prodList = ticket.getProductsList();
        Map<Product, Double> hasDiscount = catCalc.discountPerProduct(ticket);
        if (!prodList.isEmpty()) {
            // Ordenamos los productos por nombre antes de imprimirlos
            ticket.sortProducts();
            System.out.println("Ticket : " + ticket.getTicketMetadata().getTicketID());
            for (Product currentProduct : prodList) {
                CLI.printText(currentProduct.toString());
                // Si el descuento no es igual a 1.0, el producto tiene descuento
                boolean hasAnyDiscount = (hasDiscount.get(currentProduct)!=1.0);
                if (hasAnyDiscount) {
                    //Si el discount es 1.0, significa que no tiene
                    double priceAfterDiscount = currentProduct.getPrice() * hasDiscount.get(currentProduct);
                    double discountAmount = currentProduct.getPrice() - priceAfterDiscount;
                    System.out.printf(" **Discount -%s%n", PRICE_FORMAT.format(discountAmount));
                } else {
                    System.out.println(); //Aplicamos salto de linea si no hay descuento
                }
            }
        }
        //Segun sale en el formato, el formato es US, el punto es el que marca el decimal.
        System.out.printf("\tTotal price: %s%n", PRICE_FORMAT.format(discountResult.getTotalWithout()));
        System.out.printf("\tTotal discount: %s%n", PRICE_FORMAT.format(discountResult.getTotalDiscount()));
        System.out.printf("\tFinal price: %s%n", PRICE_FORMAT.format(discountResult.getTotalWith()));
    }

    public void printFinalTicket(Ticket<? extends Product> ticket){
        printCurrentTicket(ticket);
    }

    public static String ticketIDFinalFormat(Ticket<? extends Product> ticket, LocalDateTime closingTime){
         return ticket.getTicketMetadata().getTicketID() + "-"
               + closingTime.format(TICKET_ID_FORMAT);
    }

}
