package test.java.upm;

import jdk.jfr.Category;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void creaProductoValido() {
        upm.Product p = new upm.Product(1, "Libro POO","Book", 25);

        assertEquals(1, p.getId());
        assertEquals("Libro POO", p.getName());
        assertEquals("Book", p.getCategory());
        assertEquals(25.0, p.getPrice());
    }

    @Test
    void idDebeSerPositivo() {
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(0, "Libro", "Book", 25));
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(-3, "Libro", "Book", 25));
    }

    @Test
    void nombreNoPuedeSerNuloNiVacioNiSoloEspacios() {
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, null, "Book", 25));
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, "", "Book", 25));
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, "   ", "Book", 25));
    }

    @Test
    void nombreNoPuedeTener100CaracteresONMas() {
        String len99 = "x".repeat(99);
        String len100 = "x".repeat(100);

        // 99 OK
        upm.Product p = new upm.Product(1, len99, "Merch", 1);
        assertEquals(len99, p.getName());

        // 100 → inválido
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, len100, "Merch", 1));
    }

    @Test
    void precioDebeSerMayorQueCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, "Libro", "Book", 0));
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, "Libro", "Book", -1));
    }

    @Test
    void gettersDevuelvenLoEsperado() {
        upm.Product p = new upm.Product(10, "Camiseta UPM", "Merch", 15);
        assertAll(
                () -> assertEquals(10, p.getId()),
                () -> assertEquals("Camiseta UPM", p.getName()),
                () -> assertEquals("Merch", p.getCategory()),
                () -> assertEquals(15, p.getPrice())
        );
    }
}

