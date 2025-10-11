package upm;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;


public class TicketTest {
//TODAVIA NO ESTA BIEN IMPLEMENTADA
//    private final PrintStream originalOut = System.out;
//    private final PrintStream originalErr = System.err;
//    private ByteArrayOutputStream out;
//    private ByteArrayOutputStream err;
//
//    @Before
//    public void setup() {
//        out = new ByteArrayOutputStream();
//        err = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(out));
//        System.setErr(new PrintStream(err));
//    }
//
//    @After
//    public void tearDown() {
//        System.setOut(originalOut);
//        System.setErr(originalErr);
//    }
//
//    /* ---------- Helpers ---------- */
//
//    private Product p(int id, String name, Category cat, double price) {
//        return new Product(id, name, cat, price);
//    }
//
//    private void seedCatalog(Product... ps) {
//        for (Product prod : ps) {
//            Catalog.addProduct(prod);
//        }
//    }
//
//    /* ---------- Tests ---------- */
//
//    @Test
//    public void aplica10PorcSiHayDosBOOK_enLineasYTotales() {
//        // 2 libros de 30 → -3.00 por línea, total -6.00, final 54.00
//        Product libro = p(1001, "Libro POO V2", Category.BOOK, 30.0);
//        seedCatalog(libro);
//
//        Ticket t = new Ticket();
//        t.addProductToTicket(1001, 2); // imprime ticket provisional
//
//        String s = out.toString().replace("\r\n", "\n");
//
//        // Dos líneas con descuento (exactamente 3.00)
//        assertEquals(2, s.lines().filter(l -> l.contains("**Discount -3.00")).count());
//
//        // Totales exactos con dos decimales y etiquetas que usa tu código
//        assertTrue(s.contains("Total price: 60.00"));
//        assertTrue(s.contains("Total discount: 6.00"));
//        assertTrue(s.contains("Final price: 54.00"));
//    }
//
//    @Test
//    public void sinDescuentoConUnaSolaUnidadPorCategoria() {
//        Product shirt = p(1002, "Camiseta talla:M UPM", Category.CLOTHES, 15.0);
//        seedCatalog(shirt);
//
//        Ticket t = new Ticket();
//        t.addProductToTicket(1002, 1);
//
//        String s = out.toString();
//        assertFalse("No debe mostrar línea de descuento con 1 unidad",
//                s.toLowerCase().contains("**discount -".toLowerCase()));
//
//        assertTrue(s.contains("Total price: 15.00"));
//        assertTrue(s.contains("Total discount: 0.00"));
//        assertTrue(s.contains("Final price: 15.00"));
//    }
//
//    @Test
//    public void imprimeOrdenadoPorNombreAlfabetico() {
//        Product libro = p(1003, "Libro POO", Category.BOOK, 30.0);
//        Product camisa = p(1004, "Camiseta UPM", Category.CLOTHES, 15.0);
//        seedCatalog(libro, camisa);
//
//        Ticket t = new Ticket();
//        t.addProductToTicket(1003, 2); // dos libros → tendrá descuento
//        t.addProductToTicket(1004, 1);
//
//        String s = out.toString().replace("\r\n", "\n");
//        // Extrae solo las líneas de producto
//        String[] productLines = s.lines()
//                .filter(l -> l.startsWith("{class:Product"))
//                .toArray(String[]::new);
//
//        assertTrue(productLines.length >= 3);
//
//        // Debe salir "Camiseta..." antes que "Libro..." por orden alfabético
//        int idxCam = -1, idxLibPrimero = -1;
//        for (int i = 0; i < productLines.length; i++) {
//            if (productLines[i].contains("name:Camiseta UPM")) idxCam = i;
//            if (productLines[i].contains("name:Libro POO") && idxLibPrimero == -1) idxLibPrimero = i;
//        }
//        assertTrue(idxCam != -1 && idxLibPrimero != -1);
//        assertTrue("La Camiseta debe imprimirse antes que el Libro", idxCam < idxLibPrimero);
//    }
//
//    @Test
//    public void removeProductFromTicket_eliminaTodasLasAparicionesYActualizaDescuentos() {
//        Product libro = p(1005, "Libro", Category.BOOK, 20.0);
//        Product merch = p(1006, "Llaveros", Category.MERCH, 5.0);
//        seedCatalog(libro, merch);
//
//        Ticket t = new Ticket();
//        t.addProductToTicket(1005, 2); // 2 libros (habrá descuento)
//        t.addProductToTicket(1006, 1);
//
//        out.reset(); // limpiamos para leer solo la impresión tras eliminar
//        t.removeProductFromTicket(1005);
//        t.printCurrentTicket();
//
//        String s = out.toString();
//        // No debe quedar ningún libro
//        assertFalse("No debe quedar el Libro tras eliminar", s.contains("name:Libro"));
//
//        // Solo merch → sin descuento
//        assertTrue(s.contains("Total discount: 0.00"));
//        assertTrue(s.contains("Final price: 5.00"));
//    }
//    public void descuentosPorCategoria_con2Unidades(Category cat, double price, int qty, String expectedTotalDiscountStr) {
//        // Cada ítem se descuenta según tu switch (factores: 0.95, 0.93, 0.9, 0.97, 1)
//        Product prod = p(2000 + cat.ordinal(), "Prod " + cat, cat, price);
//        seedCatalog(prod);
//
//        Ticket t = new Ticket();
//        t.addProductToTicket(prod.getId(), qty);
//
//        String s = out.toString();
//        assertTrue(s.contains("Total price: " + String.format("%.2f", price * qty)));
//
//        // Comprueba el total de descuento exacto en tu formato
//        assertTrue(s.contains("Total discount: " + expectedTotalDiscountStr));
//    }
//
//    @Test
//    public void getTotalPriceAndDiscounts_devuelveArregloConTotalesEnOrden() {
//        Product a = p(1010, "AAA", Category.BOOK, 30.0);       // dos → 10% c/u
//        Product b = p(1011, "BBB", Category.CLOTHES, 15.0);    // uno → 0%
//        seedCatalog(a, b);
//
//        Ticket t = new Ticket();
//        t.addProductToTicket(1010, 2);
//        t.addProductToTicket(1011, 1);
//
//        double[] arr = t.getTotalPriceAndDiscounts();
//        assertEquals(75.0, arr[0], 1e-6);  // finalPriceWithoutDiscount
//        assertEquals(69.0, arr[1], 1e-6);  // finalPriceWithDiscount
//        assertEquals(6.0,  arr[2], 1e-6);  // totalDiscount
//    }
}
