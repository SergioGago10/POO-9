package upm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
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
        boolean isIteractive = (args.length == 0); //False si se pasa archivo, true si escribimos por el command line
        System.setErr(System.out);
        //Esto hace que todos los errores sean system.out en vez de system.err, lo hago por un error que ocurre en la salida en la consola, no van a la misma
        // velocidad y eso provoca que los system.out se impriman antes que los system.err, dando lugar a texto mal puesto
        // No queda igual de bonito, pero es la unica solución que he podido encontrar

        Ticket ticketActual = new Ticket(); //Antes de iniciar el programa se crea un ticket desde 0
        if (scanner == null) {
            return;
        }
        System.out.println("Ticket module. Type 'help' to see commands.");
        while (true) {
            System.out.print("tUPM> ");
            String userInput = scanner.nextLine().trim(); //El trim evita que tengamos espacios al final y al inicio del string

            if(!isIteractive){
                System.out.println(userInput);
            }

            if (userInput.isEmpty()) continue; //Esto se hace por si se da enter simplemente ignorando la información vacía y no poner el mensaje de error de comando que sería molesto

            String[] arrayUserInput = userInput.split(" ");
            try {
                switch (arrayUserInput[0].toLowerCase()) {
                    case "help":
                        helpCommand();
                        break;
                    case "echo":
                        System.out.print("echo ");
                        for (int i = 1; i < arrayUserInput.length; i++) {
                            System.out.print(arrayUserInput[i] + " ");
                        }
                        System.out.println();
                        break;
                    case "exit":
                        return;
                    case "prod":
                        handleProdCommand(arrayUserInput);
                        break;

                    case "ticket":
                        ticketActual = handleTicketCommand(arrayUserInput, ticketActual);
                        break;
                    default:
                        System.err.println("Command not found. Type 'help' to see the command list.");
                }
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void handleProdCommand(String[] arrayUserInput) {
        if (arrayUserInput.length < 2) {
            System.err.println("Usage: prod <add|list|update|remove>");
            return;
        }

        String prodCmd = arrayUserInput[1].toLowerCase();
        switch (prodCmd) {
            case "add":
                if (arrayUserInput.length < 6 || !arrayUserInput[3].contains("\"")) {
                    System.err.println("Usage: prod add <id> \"<nombre>\" <categoria> <precio>");
                } else {
                    try {
                        prodAddCommand(arrayUserInput);
                    } catch (Exception e) {
                        System.err.println("Error adding product: " + e.getMessage());
                    }
                }
                break;

            case "list":
                prodListCommand();
                System.out.println("prod list: ok");
                break;

            case "update":
                if (arrayUserInput.length < 5) {
                    System.err.println("Usage: prod update <id> <campo> <valor> (campo: nombre|categoria|precio)");
                } else {
                    try {
                        prodUpdateCommand(arrayUserInput);
                    } catch (Exception e) {
                        System.err.println("Error updating product: " + e.getMessage());
                    }
                }
                break;

            case "remove":
                if (arrayUserInput.length != 3) {
                    System.err.println("Usage: prod remove <id>");
                } else {
                    try {
                        prodRemoveCommand(arrayUserInput);
                    } catch (Exception e) {
                        System.err.println("Error removing product: " + e.getMessage());
                    }
                }
                break;

            default:
                System.err.println("Unknown prod command. Type 'help' to see the command list.");
        }
    }

    private Ticket handleTicketCommand(String[] arrayUserInput, Ticket ticketActual) {
        if (arrayUserInput.length < 2) {
            System.err.println("Usage: ticket <new|add|remove|print>");
            return ticketActual;
        }
        String ticketCmd = arrayUserInput[1].toLowerCase();
        switch (ticketCmd) {
            case "new":
                System.out.println("ticket new: ok");
                return new Ticket();
            case "add":
                if (arrayUserInput.length != 4) {
                    System.err.println("Usage: ticket add <prodId> <cantidad>");
                } else {
                    try {
                        int prodId = Integer.parseInt(arrayUserInput[2]); //El id al que se accede es el elemento del array por lo que se debe de restar un numero
                        int cantidad = Integer.parseInt(arrayUserInput[3]);
                        if (Catalog.idExists(prodId)) {
                            ticketActual.addProductToTicket(prodId, cantidad);
                        } else {
                            System.err.println("prodId must be an id contained in the catalog. Type 'prod list' to see all the catalog.");
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("prodId and cantidad must be integers.");
                    } catch (Exception e) {
                        System.err.println("Error adding product to ticket: " + e.getMessage());
                    }
                }
                break;

            case "remove":
                if (arrayUserInput.length != 3) {
                    System.err.println("Usage: ticket remove <prodId>");
                } else {
                    try {
                        int prodId = Integer.parseInt(arrayUserInput[2]);
                        if (Catalog.idExists(prodId)) {
                            ticketActual.removeProductFromTicket(prodId);
                            System.out.println("ticket remove: ok");
                        } else
                            System.out.println("Product with the id " + prodId + " didn't found.");
                    } catch (NumberFormatException e) {
                        System.err.println("prodId must be an integer.");
                    } catch (Exception e) {
                        System.err.println("Error removing product from ticket: " + e.getMessage());
                    }
                }
                break;

            case "print":
                try {
                    ticketActual.printCurrentTicket();
                    System.out.println("ticket print: ok");
                } catch (Exception e) {
                    System.err.println("Error printing ticket: " + e.getMessage());
                }
                break;

            default:
                System.err.println("Unknown ticket command. Type 'help' to see the command list.");
        }
        return ticketActual;
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
        try {
            int i = 2;
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
                } while (!arrayUserInput[i].endsWith("\""));
                if (name.length() > Product.getMaxCharName())
                    System.err.println("Maximun " + Product.getMaxCharName() + " characteres on name");
                else {
                    i++;
                    //"Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC."
                    Category category = Category.valueOf(arrayUserInput[i].toUpperCase());
                    i++;
                    double price = Double.parseDouble(arrayUserInput[i]);
                    Product product = new Product(id, name.toString(), category, price);
                    Catalog.addProduct(product);
                    //Imprimimos por pantalla el producto que hemos puesto
                    System.out.print("{class:" + product.getClass().getSimpleName());
                    System.out.print(", id:" + product.getId());
                    System.out.print(", name:" + product.getName());
                    System.out.print(", Category:" + product.getCategory());
                    System.out.printf(", price: %.2f", product.getPrice());
                    System.out.println("}");
                    System.out.println("prod add: ok");
                }
            }
        } catch (NumberFormatException exception) {
            System.err.print("Id must be an integer number and price must be a decimal number.");
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.err.print("Usage: prod add <id> \"<name>\" <category> <price>");
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        }
    }

    private void prodListCommand() {
        List<Product> productList = Catalog.getCatalog();
        if (productList.isEmpty()) {
            System.out.println("The catalog is empty.");
        } else {
            System.out.println("Catalog:");
            for (int j = 0; j < Catalog.getAmountProducts(); j++) {
                System.out.print("  {class:" + productList.get(j).getClass().getSimpleName());
                System.out.print(",id: " + productList.get(j).getId());
                System.out.print(",name:" + productList.get(j).getName());
                System.out.print(",Category:" + productList.get(j).getCategory()); // no se si lo imprime bien
                System.out.printf(",price: %.2f", productList.get(j).getPrice());
                System.out.println("}");
            }
        }
    }


    private void prodUpdateCommand(String[] arrayUserInput) {
        try {
            boolean updated;
            int id = Integer.parseInt(arrayUserInput[2]);
            Product updatedProduct = Catalog.getProduct(id);
            int index = Catalog.indexOfProduct(id);
            if (index != -1 && updatedProduct != null) {
                switch (arrayUserInput[3]) {
                    case "NAME":
                        StringBuilder name = new StringBuilder(Product.getMaxCharName());
                        int i = 3;
                        do {
                            i++;
                            if (i != 4)
                                name.append(" ");
                            name.append(arrayUserInput[i]);
                        } while (!arrayUserInput[i].endsWith("\""));
                        updatedProduct.setName(name.toString());
                        updated = true;
                        break;
                    case "PRICE":
                        String price = arrayUserInput[4];
                        double newPrice = Double.parseDouble(price);
                        updatedProduct.setPrice(newPrice);
                        updated = true;
                        break;
                    case "CATEGORY":
                        Category category = Category.valueOf(arrayUserInput[4].toUpperCase());
                        updatedProduct.setCategory(category);
                        updated = true;
                        break;
                    default:
                        System.err.println("Only allowed update on NAME, PRICE OR CATEGORY");
                        updated = false;
                }
                //Imprimimos por pantalla lo que hemos actualizado
                if (updated) { //Esto es simplemente para evitar un NullPointerException, algo que no ocurriría nunca, pero por si acaso
                    System.out.print("{class:" + updatedProduct.getClass().getSimpleName());
                    System.out.print(",id: " + updatedProduct.getId());
                    System.out.print(",name:" + updatedProduct.getName());
                    System.out.print(",Category:" + updatedProduct.getCategory());
                    System.out.printf(",price: %.2f", updatedProduct.getPrice());
                    System.out.println("}");
                    System.out.println("Prod update: ok");
                }
            } else {
                System.err.println("Product with id " + id + " didn't found.");
            }
        } catch (NumberFormatException exception) {
            System.err.print("Id must be an integer number and price must be a decimal number.");
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.err.print("Name field must be between \"\"");
        }catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        }
    }

    private void prodRemoveCommand(String[] arrayUserInput) {
        try {
            int id = Integer.parseInt(arrayUserInput[2]);
            Product productRemoved = Catalog.getProduct(id);
            if (productRemoved != null) { //Esto ya directamente comprueba si se puede eliminar o no por lo que no importa no comprobarlo antes
                System.out.print("{class:" + productRemoved.getClass().getSimpleName() +
                        ",id:" + productRemoved.getId() +
                        ",name:" + productRemoved.getName() +
                        ",Category:" + productRemoved.getCategory());
                System.out.printf(",price: %.2f}\n", productRemoved.getPrice());
                if (Catalog.remove(id))
                    System.out.println("prod remove: ok");
            } else {
                System.err.println("The product with the id:" + id + " couldn't be removed. Product not found.");
            }
        } catch (NumberFormatException exception) {
            System.err.print("Id must be an integer number.");
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
