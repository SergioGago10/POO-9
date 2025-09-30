package upm;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.run();
        app.close();
    }

    private void init() {
        System.out.println("Welcome to the ticket module App.");
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
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
                    switch (arrayUserInput[1]) {
                        case "add":
                            prodAddCommand(arrayUserInput);
                            break;
                        case "list":
                            prodListCommand();
                            break;
                    }
            }
        }
    }

    private void close() {
        System.out.println("Closing application.\nGoodbye!");
        System.exit(0);
    }

    private void helpCommand(){
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

    private void prodAddCommand(String[] arrayUserInput){
        int i=2;
        int id = Integer.parseInt(arrayUserInput[2]);
        StringBuilder name = new StringBuilder(Product.getMaxCharName());
        i++;
        do {
            name.append(arrayUserInput[i]);
            name.append(" ");
            i++;
        } while (!arrayUserInput[i].contains("\""));
        name.append(arrayUserInput[i]);
        i++;
        String category=arrayUserInput[i];
        i++;
        int price = Integer.parseInt(arrayUserInput[i]);
        Product product = new Product(id, name.toString(), category, price);
        Product.addProduct(product);
        System.out.println("ok");
    }

    private void prodListCommand(){
        Product[] productList = Product.getProductList();
        System.out.println("Catalog:");
        for (int j = 0; j < Product.getAmountProducts(); j++) {
            System.out.print("Id: "+productList[j].getId());
            System.out.print(", name:"+productList[j].getName());
            System.out.print(", Category:"+productList[j].getCategory()); // no se si lo imprime bien
            System.out.println(", price:"+productList[j].getPrice());
        }
    }
}
