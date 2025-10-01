package upm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.run(args);
        app.close();
    }

    private void init() {
        System.out.println("Welcome to the ticket module App.");
    }

    private void run(String[] args) {
        Scanner scanner = createScanner(args);
        if (scanner == null) {
            return;
        }
        System.out.println("Ticket module. Type 'help' to see commands.");
        while (true) {
            System.out.print("tUPM> ");
            String userInput = scanner.nextLine();
            String[] arrayUserInput = userInput.split(" ");
            switch (arrayUserInput[0]) {
                case "help":
                    helpCommand();
                    break;
                case "echo":
                    for (int i = 1; i < arrayUserInput.length; i++) {
                        System.out.print(arrayUserInput[i] + " ");
                    }
                    System.out.println();
                    break;
                case "exit":
                    return;
                case "prod":
                    if(arrayUserInput.length>1){
                        switch (arrayUserInput[1]) {
                            case "add":
                                prodAddCommand(arrayUserInput);
                                break;
                            case "list":
                                prodListCommand();
                                break;
                            case "update":
                                prodUpdateCommand(arrayUserInput);
                                break;
                        }
                    }else{
                        System.err.println("Command not found. Type 'help' to see the command list.");
                        break;
                    }

                case "ticket":
                    //Completar
                    break;
                default:
                    System.err.println("Command not found. Type 'help' to see the command list.");
            }
        }
    }

    private void close() {
        System.out.println("Closing application.\nGoodbye!");
        System.exit(0);
    }

    private void helpCommand() {
        System.out.println("Commands:\n prod add <id> \"<name>\" <category> <price>\n" +
                " prod list\n" +
                " prod update <id> NAME|CATEGORY|PRICE <value>\n" +
                " prod remove <id>\n" +
                " ticket new\n" +
                " ticket add <prodId> <quantity>\n" +
                " ticket remove <prodId>\n" +
                " ticket print\n" +
                " echo \"<texto>\"\n" +
                " help\n" +
                " exit\n\n" + "Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS\n" +
                "Discounts if there are ≥2 units in the category: MERCH 0%," +
                " STATIONERY 5%, CLOTHES 7%, BOOK 10%,\n" +
                "ELECTRONICS 3%.");
    }

    private void prodAddCommand(String[] arrayUserInput) {
        int i = 2;
        char[] arrayChars;
        int id = Integer.parseInt(arrayUserInput[2]);
        if (Catalog.idExists(id)) {
            System.err.println("Product with id " + id + " already exist.");
        } else {
            StringBuilder name = new StringBuilder(Product.getMaxCharName());
            do {
                i++;
                if (i != 3)
                    name.append(" ");
                name.append(arrayUserInput[i]);
                arrayChars = arrayUserInput[i].toCharArray();
            } while (arrayChars[arrayChars.length - 1] != '\"');
            i++;
            String category = arrayUserInput[i];
            i++;
            int price = Integer.parseInt(arrayUserInput[i]);
            Product product = new Product(id, name.toString(), category, price);
            Catalog.addProduct(product);
        }
    }

    private void prodListCommand() {
        Product[] productList = Catalog.getCatalog();
        System.out.println("Catalog:");
        for (int j = 0; j < Catalog.getAmountProducts(); j++) {
            System.out.print("Id: " + productList[j].getId());
            System.out.print(", name:" + productList[j].getName());
            System.out.print(", Category:" + productList[j].getCategory()); // no se si lo imprime bien
            System.out.println(", price:" + productList[j].getPrice());
        }
    }


    private void prodUpdateCommand(String[] arrayUserInput) {
        int id = Integer.parseInt(arrayUserInput[2]);
        int index = Catalog.indexOfProduct(id);
        String value = arrayUserInput[4];
        if (index != -1) {
            switch (arrayUserInput[3]) {
                case "NAME":
                    Catalog.getCatalog()[index].setName(value);
                    break;
                case "PRICE":
                    int newPrice = Integer.parseInt(value);
                    Catalog.getCatalog()[index].setPrice(newPrice);
                    break;
                case "CATEGORY":
                    Catalog.getCatalog()[index].setCategory(value);
                    break;
            }
        }

    }

    private Scanner createScanner(String[] args) {
        Scanner scanner = null;
        try {
            if (args.length == 0)
                scanner = new Scanner(System.in);
            else {
                File file = new File(args[0]);
                scanner = new Scanner(file);
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return scanner;
    }
}
