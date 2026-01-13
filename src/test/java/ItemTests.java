import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.commands.product.ProdAddCommand;
import upm.products.BasicProduct;
import upm.products.Category;
import upm.products.Product;
import upm.products.ProductManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class ItemTests {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void createProduct() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add 1 \"Libro POO\" BOOK 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        BasicProduct expectedbasicProduct = new BasicProduct("1", "'Libro POO'", Category.BOOK, 25);
        ProductManager productManager = ProductManager.getInstance();
        List<Product> products = productManager.getCatalogProducts();
        BasicProduct realProduct = (BasicProduct) products.get(0);
        Assertions.assertEquals(expectedbasicProduct.getName(), realProduct.getName());
        Assertions.assertEquals(expectedbasicProduct.getCategory(), realProduct.getCategory());
        Assertions.assertEquals(expectedbasicProduct.getId(), realProduct.getId());
        Assertions.assertEquals(expectedbasicProduct.getPrice(), realProduct.getPrice());
    }

    @Test
    public void createProductBadId() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add -1 \"Libro POO\" BOOK 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Id must be positive.", outContent.toString().trim());
    }

    @Test
    public void createProductNegativePrice() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add 1 \"Libro POO\" BOOK -25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Price must be positive.", outContent.toString().trim());
    }

}


