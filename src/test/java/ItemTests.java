import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.commands.product.ProdAddCommand;
import upm.commands.product.ProdAddFoodCommand;
import upm.products.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public void createBasicProduct() {
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
    public void createBasicProductBadId() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add -1 \"Libro POO\" BOOK 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Id must be positive.", outContent.toString().trim());
    }

    @Test
    public void createBasicProductNegativePrice() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add 1 \"Libro POO\" BOOK -25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Price must be positive.", outContent.toString().trim());
    }

    @Test
    public void createBasicProductToMuchChars() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add 1 \"Libro POOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\" BOOK 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Name length must be between 0 and " + ProductManager.MAX_CHAR_NAME,
                outContent.toString().trim());
    }

    @Test
    public void createBasicProductBadCategory() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add 1 \"Libro POO\" LIBRO 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC",
                outContent.toString().trim());
    }

    @Test
    public void createBasicProductCharId() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add A \"Libro POO\" BOOK 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Id must be a number",
                outContent.toString().trim());
    }

    @Test
    public void createBasicProductCharPrice() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add 1 \"Libro POO\" BOOK Ñ";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Price must be double",
                outContent.toString().trim());
    }

    @Test
    public void createCustomProductCharMaxPers() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add 1 \"Libro POO\" BOOK 25 A";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Max personalization must be integer",
                outContent.toString().trim());
    }

    @Test
    public void lackOfParametersAddCommand() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Format must be: prod add ([<id>] \"<name>\" <category> <price> [<maxPers>]) || " +
                        "(\"<expiration:yyyy-MM-dd>\" <category> )",
                outContent.toString().trim());
    }

    @Test
    public void createBasicProductNoQuotesName() {
        ProdAddCommand prodCommands = new ProdAddCommand();
        String input = "prod add 1 Libro POO BOOK 25 A";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Name must be between quotes (\" \")",
                outContent.toString().trim());
    }

    @Test
    public void createFood() {
        ProdAddFoodCommand prodCommands = new ProdAddFoodCommand();
        String input = "prod addFood 23459 \"Restaurante Asador\" 50 2030-12-21 40";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        LocalDateTime date = LocalDate.of(2030, 12, 21).atStartOfDay();
        Event expectedEvent = new Event("23459", "'Restaurante Asador'", 50, date,
                40, TypeEvent.FOOD);
        ProductManager productManager = ProductManager.getInstance();
        List<Product> products = productManager.getCatalogProducts();
        Event realFood = (Event) products.get(0);
        Assertions.assertEquals(expectedEvent.getName(), realFood.getName());
        Assertions.assertEquals(expectedEvent.getTypeEvent(), realFood.getTypeEvent());
        Assertions.assertEquals(expectedEvent.getId(), realFood.getId());
        Assertions.assertEquals(expectedEvent.getPrice(), realFood.getPrice());
        Assertions.assertEquals(expectedEvent.getMaxParticipants(), realFood.getMaxParticipants());
        Assertions.assertEquals(expectedEvent.getPlannedDate(), realFood.getPlannedDate());
    }

    @Test
    public void createFoodTooLate() {
        ProdAddFoodCommand prodCommands = new ProdAddFoodCommand();
        String input = "prod addFood 23459 \"Restaurante Asador\" 50 2025-12-21 40";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("The meeting should be planned at least 3 days before",
                outContent.toString().trim());
    }

}


