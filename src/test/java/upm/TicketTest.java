package test.java.upm;
import upm.Product;
import upm.Ticket;
import jdk.jfr.Category;


import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream out;
    private ByteArrayOutputStream err;

    @BeforeEach
    void setup() {
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    /* ---------- Helpers ---------- */

    private Product p(int id, String name, String cat, int price) {
        return new Product(id, name, cat, price);
    }

    private void seedCatalog(Product... ps) {
        for (Product p : ps) {
            upm.Catalog.addProduct(p);
        }
    }

    /* ---------- Tests ---------- */

    @Test
    void aplica10PorcSiHayDosBOOK_enLineasYTotales() {
        // 2 libros de 30 → -3.00 por línea, total -6.00, final 54.00
        Product libro = p(1001, "Libro POO V2", "BOOK", 30);
        seedCatalog(libro);

        Ticket t = new upm.Ticket();
        t.addProductToTicket(1001, 2); // imprime ticket provisional

        String s = out.toString().replace("\r\n", "\n");

        // Dos líneas con descuento (exactamente 3.00)
        assertEquals(2, s.lines().filter(l -> l.contains("**Discount -3.00")).count());

        // Totales exactos con dos decimales y etiquetas que usa tu código
        assertTrue(s.contains("Total price: 60.00"));
        assertTrue(s.contains("Total discount: 6.00"));
        assertTrue(s.contains("Final price: 54.00"));
    }

    @Test
    void sinDescuentoConUnaSolaUnidadPorCategoria() {
        Product shirt = p(1002, "Camiseta talla:M UPM", "Clothes", 15);
        seedCatalog(shirt);

        Ticket t = new Ticket();
        t.addProductToTicket(1002, 1);

        String s = out.toString();
        assertFalse(s.contains("**Discount -"), "No debe mostrar línea de descuento con 1 unidad");
        assertTrue(s.contains("Total price: 15.00"));
        assertTrue(s.contains("Total discount: 0.00"));
        assertTrue(s.contains("Final price: 15.00"));
    }

    @Test
    void imprimeOrdenadoPorNombreAlfabetico() {
        Product libro = p(1003, "Libro POO", "BOOK", 30);
        Product camisa = p(1004, "Camiseta UPM", "CLOTHES", 15.);
        seedCatalog(libro, camisa);

        Ticket t = new Ticket();
        t.addProductToTicket(1003, 2); // dos libros → tendrá descuento
        t.addProductToTicket(1004, 1);

        String s = out.toString().replace("\r\n", "\n");
        // Extrae solo las líneas de producto
        String[] productLines = s.lines()
                .filter(l -> l.startsWith("{class:Product"))
                .toArray(String[]::new);

        assertTrue(productLines.length >= 3);

        // Debe salir "Camiseta..." antes que "Libro..." por orden alfabético
        int idxCam = -1, idxLibPrimero = -1;
        for (int i = 0; i < productLines.length; i++) {
            if (productLines[i].contains("name:Camiseta UPM")) idxCam = i;
            if (productLines[i].contains("name:Libro POO") && idxLibPrimero == -1) idxLibPrimero = i;
        }
        assertTrue(idxCam != -1 && idxLibPrimero != -1);
        assertTrue(idxCam < idxLibPrimero, "La Camiseta debe imprimirse antes que el Libro");
    }

    @Test
    void removeProductFromTicket_eliminaTodasLasAparicionesYActualizaDescuentos() {
        Product libro = p(1005, "Libro", "BOOK", 20);
        Product merch = p(1006, "Llaveros", "MERCH", 5);
        seedCatalog(libro, merch);

        Ticket t = new Ticket();
        t.addProductToTicket(1005, 2); // 2 libros (habrá descuento)
        t.addProductToTicket(1006, 1);

        out.reset(); // limpiamos para leer solo la impresión tras eliminar
        t.removeProductFromTicket(1005);
        t.printCurrentTicket();

        String s = out.toString();
        // No debe quedar ningún libro
        assertFalse(s.contains("name:Libro"), "No debe quedar el Libro tras eliminar");

        // Solo merch → sin descuento
        assertTrue(s.contains("Total discount: 0.00"));
        assertTrue(s.contains("Final price: 5.00"));
    }

    @Test
    void limiteDe100Productos_enTicket_addMuestraErrorYNoSuperaMaximo() {
        Product libro = p(1007, "AAA", "BOOK", 1);
        seedCatalog(libro);

        Ticket t = new Ticket();
        t.addProductToTicket(1007, 101); // intenta 101

        String printed = out.toString().replace("\r\n", "\n");
        // Cuenta cuántas líneas de producto se imprimieron
        long productLines = printed.lines().filter(l -> l.startsWith("{class:Product")).count();
        assertEquals(100, productLines, "No debe superar 100 productos en el ticket");

        // Debe avisar por System.err
        String errMsg = err.toString();
        assertTrue(errMsg.contains("You can't add more products"), "Debe avisar del límite en stderr");
    }

    @ParameterizedTest
    @CsvSource({
            "STATIONERY, 100.0, 2, 10.00",   // 5% * 2 = 10
            "CLOTHES,    100.0, 2, 14.00",   // 7% * 2 = 14
            "BOOK,       100.0, 2, 20.00",   // 10% * 2 = 20
            "ELECTRONIC, 100.0, 2, 6.00",    // 3% * 2 = 6
            "MERCH,      100.0, 2, 0.00"     // 0% * 2 = 0
    })
    void descuentosPorCategoria_con2Unidades(Category cat, double price, int qty, String expectedTotalDiscountStr) {
        // Cada ítem se descuenta según tu switch (factores: 0.95, 0.93, 0.9, 0.97, 1)
        Product p = p(2000 + cat.ordinal(), "Prod " + cat, cat, price);
        seedCatalog(p);

        Ticket t = new Ticket();
        t.addProductToTicket(p.getId(), qty);

        String s = out.toString();
        assertTrue(s.contains("Total price: " + String.format("%.2f", price * qty)));

        // Comprueba el total de descuento exacto en tu formato
        assertTrue(s.contains("Total discount: " + expectedTotalDiscountStr));
    }

    @Test
    void getTotalPriceAndDiscounts_devuelveArregloConTotalesEnOrden() {
        Product a = p(1010, "AAA", Category.BOOK, 30.0);       // dos → 10% c/u
        Product b = p(1011, "BBB", Category.CLOTHES, 15.0);    // uno → 0%
        seedCatalog(a, b);

        Ticket t = new Ticket();
        t.addProductToTicket(1010, 2);
        t.addProductToTicket(1011, 1);

        double[] arr = t.getTotalPriceAndDiscounts();
        assertEquals(75.0, arr[0], 1e-6);  // finalPriceWithoutDiscount
        assertEquals(69.0, arr[1], 1e-6);  // finalPriceWithDiscount
        assertEquals(6.0,  arr[2], 1e-6);  // totalDiscount
    }
}

