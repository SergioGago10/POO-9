package upm;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CatalogTest {

    @Before
    public void resetCatalog() {
        //Reiniciar catalogo despues de cualquier test
        Product[] arr = Catalog.getCatalog();
        for (int i = 0; i < arr.length; i++) arr[i] = null;

        try {
            var field = Catalog.class.getDeclaredField("amountProducts");
            field.setAccessible(true);
            field.setInt(null, 0);
        } catch (Exception ignored) {}
    }

    @Test
    public void addProduct_incrementaCantidadYSePuedeRecuperar() {
        Product p = new Product(1, "Libro", Category.BOOK, 25.0);
        Catalog.addProduct(p);

        assertEquals(1, Catalog.getAmountProducts());
        Product obtenido = Catalog.getProduct(1);
        assertNotNull(obtenido);
        assertEquals("Libro", obtenido.getName());
        assertEquals(Category.BOOK, obtenido.getCategory());
    }

    @Test
    public void getProduct_devuelveNullSiNoExiste() {
        Product p = new Product(1, "A", Category.MERCH, 10.0);
        Catalog.addProduct(p);

        assertNull(Catalog.getProduct(999)); // id inexistente
    }

    @Test
    public void idExists_funcionaCorrectamente() {
        Product p = new Product(5, "Pendrive", Category.ELECTRONIC, 12.0);
        Catalog.addProduct(p);

        assertTrue(Catalog.idExists(5));
        assertFalse(Catalog.idExists(8));
    }

    @Test
    public void indexOfProduct_devuelveIndiceCorrecto() {
        Product p1 = new Product(1, "A", Category.MERCH, 1.0);
        Product p2 = new Product(2, "B", Category.BOOK, 2.0);
        Catalog.addProduct(p1);
        Catalog.addProduct(p2);

        assertEquals(0, Catalog.indexOfProduct(1));
        assertEquals(1, Catalog.indexOfProduct(2));
        assertEquals(-1, Catalog.indexOfProduct(99));
    }

    @Test
    public void remove_eliminaYDesplazaElementos() {
        Product p1 = new Product(1, "A", Category.MERCH, 1.0);
        Product p2 = new Product(2, "B", Category.BOOK, 2.0);
        Product p3 = new Product(3, "C", Category.CLOTHES, 3.0);
        Catalog.addProduct(p1);
        Catalog.addProduct(p2);
        Catalog.addProduct(p3);

        boolean removed = Catalog.remove(2);
        assertTrue(removed);
        assertEquals(2, Catalog.getAmountProducts());

        Product[] cat = Catalog.getCatalog();
        assertEquals(3, cat[1].getId());
        assertFalse(Catalog.idExists(2));
    }

    @Test
    public void remove_devuelveFalseSiNoExiste() {
        Product p = new Product(1, "A", Category.MERCH, 1.0);
        Catalog.addProduct(p);

        boolean removed = Catalog.remove(99);
        assertFalse(removed);
        assertEquals(1, Catalog.getAmountProducts());
    }

    @Test
    public void isEmpty_trueSiNoHayProductos() {
        assertTrue(Catalog.isEmpty());
        Product p = new Product(1, "A", Category.MERCH, 1.0);
        Catalog.addProduct(p);
        assertFalse(Catalog.isEmpty());
    }

    @Test
    public void addProduct_noRompeCuandoSeSuperaMaximo() {

        for (int i = 0; i < 200; i++) {
            Catalog.addProduct(new Product(i + 1, "Prod" + i, Category.MERCH, 1.0));
        }
        assertEquals(200, Catalog.getAmountProducts());

        try {
            Catalog.addProduct(new Product(9999, "Extra", Category.MERCH, 1.0));

        } catch (Exception e) {
            fail("El método addProduct lanzó una excepción al superar el máximo: " + e.getMessage());
        }
    }

}

