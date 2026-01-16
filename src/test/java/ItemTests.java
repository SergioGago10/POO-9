import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upm.commands.product.ProdAddCommand;
import upm.commands.product.ProdAddFoodCommand;
import upm.commands.product.ProdAddMeetingCommand;
import upm.commands.product.ProdCommands;
import upm.products.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
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
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add -1 \"Libro POO\" BOOK 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Id must be positive.\u001B[0m", outContent.toString().trim());
    }

    @Test
    public void createBasicProductNegativePrice() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 1 \"Libro POO\" BOOK -25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Price must be positive.\u001B[0m", outContent.toString().trim());
    }

    @Test
    public void createBasicProductToMuchChars() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 1 \"Libro POOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +
                "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\" BOOK 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Name length must be between 0 and " + ProductManager.MAX_CHAR_NAME+"\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createBasicProductBadCategory() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 1 \"Libro POO\" LIBRO 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createBasicProductCharId() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add A \"Libro POO\" BOOK 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Id must be a number\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createBasicProductCharPrice() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 1 \"Libro POO\" BOOK Ñ";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Price must be double\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createCustomProductCharMaxPers() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 1 \"Libro POO\" BOOK 25 A";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Max personalization must be integer\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void lackOfParametersAddCommand() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Format must be: prod add ([<id>] \"<name>\" <category> <price> [<maxPers>]) || " +
                        "(\"<expiration:yyyy-MM-dd>\" <category> )\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createBasicProductNoQuotesName() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 1 Libro POO BOOK 25 A";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Name must be between quotes (\" \")\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createFood() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod addFood 23459 \"Restaurante Asador\" 50 2030-12-21 40";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        LocalDateTime date = LocalDate.of(2030, 12, 21).atStartOfDay();
        Event expectedEvent = new Event("23459", "'Restaurante Asador'", 50, date,
                40, TypeEvent.FOOD);
        ProductManager productManager = ProductManager.getInstance();
        List<Product> products = productManager.getCatalogProducts();
        Event realFood = (Event) products.get(products.size()-1);
        Assertions.assertEquals(expectedEvent.getName(), realFood.getName());
        Assertions.assertEquals(expectedEvent.getTypeEvent(), realFood.getTypeEvent());
        Assertions.assertEquals(expectedEvent.getId(), realFood.getId());
        Assertions.assertEquals(expectedEvent.getPrice(), realFood.getPrice());
        Assertions.assertEquals(expectedEvent.getMaxParticipants(), realFood.getMaxParticipants());
        Assertions.assertEquals(expectedEvent.getPlannedDate(), realFood.getPlannedDate());
    }

    @Test
    public void createFoodTooLate() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod addFood 23459 \"Restaurante Asador\" 50 2025-12-21 40";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> The food should be planned at least 3 days before\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createMeetingTooLate() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod addMeeting 23456 \"Reunion Rotonda\" 12 2025-12-21 100";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> The meeting should be planned at least 12 hours before\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createMeetingWrongDate() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod addMeeting 23456 \"Reunion Rotonda\" 12 21-12-2025 100";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Expiration date must have the next format: yyyy-mm-dd\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createFoodWrongDate() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod addFood 23459 \"Restaurante Asador\" 50 21-12-2025 40";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Expiration date must have the next format: yyyy-mm-dd\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createMeetingMaxPeopleChar() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod addMeeting 23456 \"Reunion Rotonda\" 12 2025-12-21 A";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Max people must be an integer number.\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createFoodMaxPeopleChar() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod addFood 23459 \"Restaurante Asador\" 50 2025-12-21 A";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Max people must be an integer number.\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createService() {
        ProductManager.getInstance().setCatalogServices(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 2035-12-24 TRANSPORT";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        ProductService expectedService=new ProductService("1S",ServiceCategory.TRANSPORT,
                LocalDate.of(2035, 12, 24).atStartOfDay());
        List<ProductService> servicesList=ProductManager.getInstance().getCatalogServices();
        ProductService realService=servicesList.get(servicesList.size()-1);
        Assertions.assertEquals(expectedService.getId(), realService.getId());
        Assertions.assertEquals(expectedService.getMaxDate(), realService.getMaxDate());
        Assertions.assertEquals(expectedService.getCategory(), realService.getCategory());
    }

    @Test
    public void createServiceTooLate() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 2025-12-24 TRANSPORT";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> The service must have a date that has not passed.\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void createServiceBadWrittenDate() {
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod add 24-12 TRANSPORT";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Expiration date must have the next format: yyyy-mm-dd\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodListCorrect(){
        ProductManager.getInstance().setCatalogServices(new ArrayList<>());
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        BasicProduct basicProduct=new BasicProduct("10", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod list";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("Catalog:  {class:Product,id: 10,name:'Libro POO',Category:BOOK,price:25,00}\r\n" +
                        "prod list: ok",
                outContent.toString().trim());
    }

    @Test
    public void prodListTooMuchParameters(){
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod list xdddddddddd";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> format must be: prod list\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodListEmpty(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProductManager.getInstance().setCatalogServices(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod list";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Catalog is empty\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodRemoveCorrectWithProduct(){
        BasicProduct basicProduct=new BasicProduct("10", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod remove 10";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("{class:Product,id: 10,name:'Libro POO',Category:BOOK,price:25,00}\r\n" +
                        "prod remove: ok",
                outContent.toString().trim());
    }

    @Test
    public void prodRemoveCorrectWithService(){
        ProductService service=new ProductService("1S",ServiceCategory.TRANSPORT,
                LocalDate.of(2035, 12, 24).atStartOfDay());
        ProductManager.getInstance().addService(service);
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod remove 1S";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("{class:ProductService, id: 1S, category:TRANSPORT, expiration:lun dic 24 00:00:00 CET 2035}\r\n" +
                        "prod remove: ok",
                outContent.toString().trim());
    }

    @Test
    public void prodRemoveBadWritten(){
        BasicProduct basicProduct=new BasicProduct("10", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod remove 10 67";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Format must be: prod remove <id>\u001B[0m",
                outContent.toString().trim());
    }


    @Test
    public void prodRemoveProdDoesntExist(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod remove 10";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> The product with the id: 10 couldn't be removed. Product not found.\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodRemoveServiceDoesntExist(){
        ProductManager.getInstance().setCatalogServices(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        String input = "prod remove 1S";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> The product with the id: 1S couldn't be removed. " +
                        "Product not found.\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdateCorrect(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        BasicProduct basicProduct=new BasicProduct("1", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        String input = "prod update 1 NAME \"Libro POO V2\"";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("{class:Product,id: 1,name:'Libro POO V2',Category:BOOK,price:25,00}\r\n" +
                        "prod update: ok",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdatePriceNegative(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        BasicProduct basicProduct=new BasicProduct("1", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        String input = "prod update 1 PRICE -20";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Price must be positive\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdatePriceChar(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        BasicProduct basicProduct=new BasicProduct("1", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        String input = "prod update 1 PRICE A";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Price must be a number\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdateCategoryProdCorrect(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        BasicProduct basicProduct=new BasicProduct("1", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        String input = "prod update 1 CATEGORY MERCH";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("{class:Product,id: 1,name:'Libro POO',Category:MERCH,price:25,00}\r\n" +
                        "prod update: ok",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdateCategoryProdButServiceCategory(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        BasicProduct basicProduct=new BasicProduct("1", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        String input = "prod update 1 CATEGORY INSURANCE";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Category must be: MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC in Basic/Custom Products," +
                        "or: INSURANCE, TRANSPORT or SHOW in Services\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdateCategoryServiceCorrect(){
        ProductManager.getInstance().setCatalogServices(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        ProductService service=new ProductService("1S",ServiceCategory.TRANSPORT,
                LocalDate.of(2035, 12, 24).atStartOfDay());
        ProductManager.getInstance().addService(service);
        String input = "prod update 1S CATEGORY INSURANCE";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("{class:ProductService, id: 1S, category:INSURANCE, expiration:lun dic 24 00:00:00 CET 2035}\r\n" +
                        "prod update: ok",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdateCategoryServiceButProdCategory(){
        ProductManager.getInstance().setCatalogServices(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        ProductService service=new ProductService("1S",ServiceCategory.TRANSPORT,
                LocalDate.of(2035, 12, 24).atStartOfDay());
        ProductManager.getInstance().addService(service);
        String input = "prod update 1S CATEGORY MERCH";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Category must be: MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC in Basic/Custom Products," +
                        "or: INSURANCE, TRANSPORT or SHOW in Services\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdateCategoryToNotCategoryProd(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        LocalDateTime date = LocalDate.of(2030, 12, 21).atStartOfDay();
        Event event=new Event("23459", "'Restaurante Asador'", 50, date,
                40, TypeEvent.FOOD);
        ProductManager.getInstance().addProduct(event);
        String input = "prod update 23459 CATEGORY MERCH";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> That type of product doesn't have category.\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdatePriceToAService(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        ProductService service=new ProductService("1S",ServiceCategory.TRANSPORT,
                LocalDate.of(2035, 12, 24).atStartOfDay());
        ProductManager.getInstance().addService(service);
        String input = "prod update 1S PRICE 25";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> This product does not have price\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdateNameToLong(){
        ProductManager.getInstance().setCatalogProducts(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        BasicProduct basicProduct=new BasicProduct("1", "'Libro POO'", Category.BOOK, 25);
        ProductManager.getInstance().addProduct(basicProduct);
        String input = "prod update 1 NAME aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> Name length must be between 0 and "+ProductManager.MAX_CHAR_NAME+"\u001B[0m",
                outContent.toString().trim());
    }

    @Test
    public void prodUpdateNameToAService(){
        ProductManager.getInstance().setCatalogServices(new ArrayList<>());
        ProdCommands prodCommands = new ProdCommands();
        ProductService service=new ProductService("1S",ServiceCategory.TRANSPORT,
                LocalDate.of(2035, 12, 24).atStartOfDay());
        ProductManager.getInstance().addService(service);
        String input = "prod update 1S NAME nombre";
        String[] args = input.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        prodCommands.apply(args);
        Assertions.assertEquals("[31mError -> This product does not have name\u001B[0m",
                outContent.toString().trim());
    }









}


