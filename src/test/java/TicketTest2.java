import org.junit.jupiter.api.Test;
import upm.products.*;
import upm.tickets.core.Ticket;
import upm.tickets.itemsaddition.*;
import upm.tickets.management.TicketManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest2 {


    // Auxiliares


    private Ticket<?> newTicket(String ticketId, String modeFlag) {
        TicketManager ticketMgr = TicketManager.getInstance();
        Ticket<?> ticket = ticketMgr.newTicket(ticketId, modeFlag); // registra en el manager
        assertNotNull(ticket, "TicketManager.newTicket devolvió null");
        return ticket;
    }

    private Ticket<?> newProductTicket(String ticketId) {  // para Basic/Custom/Event
        return newTicket(ticketId, "-p");
    }

    private Ticket<?> newServiceTicket(String ticketId) {  // para ProductService
        return newTicket(ticketId, "-s");
    }

    // Captura stdout + stderr (por si CLI imprime en uno u otro)
    private String captureOutAndErr(Runnable action) {
        PrintStream prevOut = System.out;
        PrintStream prevErr = System.err;

        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outBuf));
        System.setErr(new PrintStream(errBuf));
        try {
            action.run();
        } finally {
            System.setOut(prevOut);
            System.setErr(prevErr);
        }
        return outBuf.toString() + errBuf.toString();
    }


    // AddBasicProduct

    @Test
    void addBasicProduct_validAmount() {
        Ticket<?> ticket = newProductTicket("T-basic-ok");

        BasicProduct prod = new BasicProduct("P1", "Bread", Category.MERCH, 1.0);
        AddBasicProduct addCmd = new AddBasicProduct();

        String ticketId = "T-basic-ok";
        String prodId = "P1";
        String qty = "2";
        String[] cliArgs = {ticketId, prodId, qty};

        boolean ok = addCmd.add(prod, cliArgs);

        assertTrue(ok);
        assertEquals(2, ticket.getItemsList().size());
    }

    @Test
    void addBasicProduct_amountZero() {
        Ticket<?> ticket = newProductTicket("T-basic-0");

        BasicProduct prod = new BasicProduct("P1", "Bread", Category.MERCH, 1.0);
        AddBasicProduct addCmd = new AddBasicProduct();

        String ticketId = "T-basic-0";
        String prodId = "P1";
        String qty = "0";
        String[] cliArgs = {ticketId, prodId, qty};

        boolean ok = addCmd.add(prod, cliArgs);

        assertFalse(ok);
        assertEquals(0, ticket.getItemsList().size());
    }


    // AddCustomProduct


    @Test
    void addCustomProduct_tooManyTexts() {
        Ticket<?> ticket = newProductTicket("T-cust-tooMany");

        CustomizableProduct customProd =
                new CustomizableProduct("C1", "Mug", Category.MERCH, 10.0, 1);

        AddCustomProduct addCmd = new AddCustomProduct();

        String ticketId = "T-cust-tooMany";
        String prodId = "C1";
        String qty = "1";
        String txt1 = "t1";
        String txt2 = "t2";
        String[] cliArgs = {ticketId, prodId, qty, txt1, txt2}; // 2 > max 1

        String output = captureOutAndErr(() -> {
            boolean ok = addCmd.add(customProd, cliArgs);
            assertFalse(ok);
        });

        assertTrue(output.contains("Too many custom texts"), "No se encontró el mensaje de error esperado.");
        assertEquals(0, ticket.getItemsList().size());
    }

    @Test
    void addCustomProduct_validTexts() {
        Ticket<?> ticket = newProductTicket("T-cust-ok");

        CustomizableProduct customProd =
                new CustomizableProduct("C1", "Mug", Category.MERCH, 10.0, 2);

        AddCustomProduct addCmd = new AddCustomProduct();

        String ticketId = "T-cust-ok";
        String prodId = "C1";
        String qty = "2";
        String personalTxt = "hello";
        String[] cliArgs = {ticketId, prodId, qty, personalTxt}; // válido

        boolean ok = addCmd.add(customProd, cliArgs);

        assertTrue(ok);
        assertEquals(2, ticket.getItemsList().size());
    }


    // AddServiceProduct


    @Test
    void addServiceProduct_expiredDate() {
        Ticket<?> ticket = newServiceTicket("T-serv-exp");

        ProductService svcProd = new ProductService(
                "S1",
                ServiceCategory.INSURANCE,
                LocalDateTime.now().minusDays(1) // expirado
        );

        AddServiceProduct addCmd = new AddServiceProduct();

        String ticketId = "T-serv-exp";
        String svcId = "S1";
        String qty = "1";
        String[] cliArgs = {ticketId, svcId, qty};

        String output = captureOutAndErr(() -> {
            boolean ok = addCmd.add(svcProd, cliArgs);
            assertFalse(ok);
        });

        assertTrue(output.contains("service date has expired"), "No se encontró el mensaje de error esperado.");
        assertEquals(0, ticket.getItemsList().size());
    }

    @Test
    void addServiceProduct_duplicate() {
        Ticket<?> ticket = newServiceTicket("T-serv-dup");

        ProductService svcProd = new ProductService(
                "S1",
                ServiceCategory.TRANSPORT,
                LocalDateTime.now().plusDays(2)
        );

        // 1ª vez: lo añadimos usando el comando (así no dependemos de equals)
        AddServiceProduct addCmd = new AddServiceProduct();

        String ticketId = "T-serv-dup";
        String svcId = "S1";
        String qty = "1";
        String[] cliArgs = {ticketId, svcId, qty};

        assertTrue(addCmd.add(svcProd, cliArgs));
        assertEquals(1, ticket.getItemsList().size());

        // 2ª vez: duplicado
        String output = captureOutAndErr(() -> {
            boolean ok2 = addCmd.add(svcProd, cliArgs);
            assertFalse(ok2);
        });

        assertTrue(output.contains("already in the ticket"), "No se encontró el mensaje de duplicado esperado.");
        assertEquals(1, ticket.getItemsList().size());
    }


    // AddEventProduct


    @Test
    void addEventProduct_foodTooSoon() {
        Ticket<?> ticket = newProductTicket("T-event-foodSoon");

        Event foodEvt = new Event(
                "E1", "Food",
                20.0,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1), // demasiado pronto para FOOD
                10,
                TypeEvent.FOOD,
                1
        );

        AddEventProduct addCmd = new AddEventProduct();

        String ticketId = "T-event-foodSoon";
        String eventId = "E1";
        String qty = "1";
        String[] cliArgs = {ticketId, eventId, qty};

        String output = captureOutAndErr(() -> {
            boolean ok = addCmd.add(foodEvt, cliArgs);
            assertFalse(ok);
        });

        assertTrue(output.contains("Foods must be planned at least 3 days"), "No se encontró el error esperado.");
        assertEquals(0, ticket.getItemsList().size());
    }

    @Test
    void addEventProduct_meetingTooSoon() {
        Ticket<?> ticket = newProductTicket("T-event-meetSoon");

        Event meetEvt = new Event(
                "E2", "Meeting",
                20.0,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2), // demasiado pronto para MEETING
                10,
                TypeEvent.MEETING,
                1
        );

        AddEventProduct addCmd = new AddEventProduct();

        String ticketId = "T-event-meetSoon";
        String eventId = "E2";
        String qty = "1";
        String[] cliArgs = {ticketId, eventId, qty};

        String output = captureOutAndErr(() -> {
            boolean ok = addCmd.add(meetEvt, cliArgs);
            assertFalse(ok);
        });

        assertTrue(output.contains("Meetings must be planned at least 12 hours"), "No se encontró el error esperado.");
        assertEquals(0, ticket.getItemsList().size());
    }

    @Test
    void addEventProduct_amountExceedsMax() {
        Ticket<?> ticket = newProductTicket("T-event-max");

        Event meetEvt = new Event(
                "E3", "Meeting",
                10.0,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                5, // maxParticipantes=5
                TypeEvent.MEETING,
                1
        );

        AddEventProduct addCmd = new AddEventProduct();

        String ticketId = "T-event-max";
        String eventId = "E3";
        String qty = "6"; // excede
        String[] cliArgs = {ticketId, eventId, qty};

        String output = captureOutAndErr(() -> {
            boolean ok = addCmd.add(meetEvt, cliArgs);
            assertFalse(ok);
        });

        assertTrue(output.contains("exceeds the limit"), "No se encontró el error esperado.");
        assertEquals(0, ticket.getItemsList().size());
    }

    @Test
    void addEventProduct_valid() {
        Ticket<?> ticket = newProductTicket("T-event-ok");

        Event meetEvt = new Event(
                "E4", "Meeting",
                10.0,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                10,
                TypeEvent.MEETING,
                1
        );

        AddEventProduct addCmd = new AddEventProduct();

        String ticketId = "T-event-ok";
        String eventId = "E4";
        String qty = "3";
        String[] cliArgs = {ticketId, eventId, qty};

        boolean ok = addCmd.add(meetEvt, cliArgs);

        assertTrue(ok);
        assertEquals(1, ticket.getItemsList().size());
    }
}



