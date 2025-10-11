package upm;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProductTest {

    @Test
    public void creaProductoValido() {
        upm.Product p = new upm.Product(1, "Libro POO", Category.BOOK, 25);

        assertEquals(1, p.getId());
        assertEquals("Libro POO", p.getName());
        assertEquals(Category.BOOK, p.getCategory());
        assertEquals(25.0, p.getPrice(),0.0);
    }

    @Test
    public void idDebeSerPositivo() {
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(0, "Libro", Category.BOOK, 25.0));
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(-3, "Libro", Category.BOOK, 25.0));
    }

    @Test
    public void nombreNoPuedeSerNuloNiVacioNiSoloEspacios() {
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, null, Category.BOOK, 25));
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, "", Category.BOOK, 25));
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, "   ", Category.BOOK, 25));
    }

    @Test
    public void nombreNoPuedeTener100CaracteresONMas() {
        String len99 = "x".repeat(99);
        String len100 = "x".repeat(100);

        // 99 bien
        upm.Product p = new upm.Product(1, len99, Category.MERCH, 1);
        assertEquals(len99, p.getName());

        // 100 → mal
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, len100, Category.MERCH, 1));
    }

    @Test
    public void precioDebeSerMayorQueCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, "Libro", Category.BOOK, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new upm.Product(1, "Libro", Category.BOOK, -1));
    }

    @Test
    public void gettersDevuelvenLoEsperado() {
        upm.Product p = new upm.Product(10, "Camiseta UPM", Category.MERCH, 15.0);

        assertEquals(10, p.getId());
        assertEquals("Camiseta UPM", p.getName());
        assertEquals(Category.MERCH, p.getCategory());
        assertEquals(15.0, p.getPrice(), 1e-6);
    }


}

