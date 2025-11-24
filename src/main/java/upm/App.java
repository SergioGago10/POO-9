package upm;

import upm.Products.BasicProduct;
import upm.Products.Catalog;
import upm.Products.Category;
import upm.Products.IProduct;
import upm.tickets.TicketManager;
import upm.tickets.Ticket;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    //Para controlar Exit
    private boolean running = true;

    private interface Command {
       //Linea separa en args[0]= comando
       //Devuelve true si encuentra el comando y lo ejecuta
        boolean execute(String[] args);
    }

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
        boolean isInteractive = (args.length == 0); // false si se pasa archivo, true si escribimos por la consola
        System.setErr(System.out);

        TicketManager ticketManager = new TicketManager(); // Antes de iniciar el programa se crea un ticketManager

        if (scanner == null) {
            return;
        }

        System.out.println("Ticket module. Type 'help' to see commands.");

        // 1) Lista de comandos
        List<Command> commands = new ArrayList<>();
        commands.add(new HelpCommand());
        commands.add(new EchoCommand());
        commands.add(new ExitCommand());
        commands.add(new ProdCommand());
        commands.add(new TicketCommand(ticketManager));

        // 2) Bucle principal de CLI
        while (running && scanner.hasNextLine()) {
            System.out.print("tUPM> ");
            String userInput = scanner.nextLine().trim(); // El trim evita espacios al principio y al final

            if (!isInteractive) {
                // Si estamos leyendo de archivo, se vuelve a mostrar el comando
                System.out.println(userInput);
            }

            if (userInput.isEmpty()) {
                // Ignoramos líneas vacías
                continue;
            }

            String[] arrayUserInput = userInput.split(" ");

            boolean handled = false; // indica si algún comando ha gestionado la entrada

            for (Command command : commands) {
                try {
                    if (command.execute(arrayUserInput)) {
                        handled = true;
                        break; // ya hay un comando que ha ejecutado esta línea
                    }
                } catch (Exception e) {
                    System.err.println("Unexpected error: " + e.getMessage());
                    handled = true; // consideramos la línea “gestionada” aunque sea con error
                    break;
                }
            }

            if (!handled) {
                System.err.println("Command not found. Type 'help' to see the command list.");
            }

            System.out.println();
        }
    }

    private class HelpCommand implements Command {
        @Override
        public boolean execute(String[] args) {
            if (args.length == 0 || !args[0].equalsIgnoreCase("help")) {
                return false; // Comando no ejecutado
            }
            helpCommand();
            return true;
        }
    }

    private class EchoCommand implements Command {
        @Override
        public boolean execute(String[] args) {
            if (args.length == 0 || !args[0].equalsIgnoreCase("echo")) {
                return false;
            }

            System.out.print("echo ");
            for (int i = 1; i < args.length; i++) {
                System.out.print(args[i] + " ");
            }
            System.out.println();
            return true;
        }
    }
    private class ExitCommand implements Command {
        @Override
        public boolean execute(String[] args) {
            if (args.length == 0 || !args[0].equalsIgnoreCase("exit")) {
                return false;
            }
            // Simplemente paramos el bucle principal
            running = false;
            return true;
        }
    }
    private class ProdCommand implements Command {
        @Override
        public boolean execute(String[] args) {
            if (args.length == 0 || !args[0].equalsIgnoreCase("prod")) {
                return false;
            }
            handleProdCommand(args);
            return true;
        }
    }
    private class TicketCommand implements Command {
        private TicketManager ticketManager;

        public TicketCommand(TicketManager ticketManager) {
            this.ticketManager = ticketManager;
        }

        @Override
        public boolean execute(String[] args) {
            if (args.length == 0 || !args[0].equalsIgnoreCase("ticket")) {
                return false;
            }
            ticketManager = handleTicketCommand(args, ticketManager);
            return true;
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

    private TicketManager handleTicketCommand(String[] arrayUserInput, TicketManager ticketManager) {
        if (arrayUserInput.length < 2) {
            System.err.println("Usage: ticket <new|add|remove|print|list>");
            return ticketManager;
        }
        String ticketCmd = arrayUserInput[1].toLowerCase();
        switch (ticketCmd) {
            case "new":
                if(arrayUserInput.length<4 || arrayUserInput.length>5){
                    System.out.println("Usage: ticket new [<id>] <cashId> <userId> ");
                } else if (arrayUserInput.length == 4) {
                    ticketManager.newTicket(Integer.parseInt(arrayUserInput[2]),Integer.parseInt(arrayUserInput[3]));
                } else{
                    ticketManager.newTicket(arrayUserInput[2],Integer.parseInt(arrayUserInput[3]),Integer.parseInt(arrayUserInput[4]));
                }
                System.out.println("ticket new: ok");
                break;
            case "add":
                if (arrayUserInput.length < 6) {
                    System.err.println("ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]");
                } else {
                    try {
                        String ticketId = arrayUserInput[2];
                        int cashId = Integer.parseInt(arrayUserInput[3]);
                        int prodId = Integer.parseInt(arrayUserInput[4]);
                        int amount = Integer.parseInt(arrayUserInput[5]);
                        if (Catalog.idExists(prodId)) {
                            Ticket ticketAModificar = ticketManager.getTicketById(ticketId);
                            if(ticketAModificar != null){
                                if(ticketAModificar.getCashId() != cashId){
                                    System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                                } else{
                                    if(arrayUserInput.length == 6){ //producto sin personalizaciones
                                        ticketAModificar.addProductToTicket(prodId,amount,null);
                                    } else{
                                        ArrayList<String> customTexts = new ArrayList<>();
                                        boolean correctFormat = true;
                                        for (int i = 6; (i < arrayUserInput.length) && (correctFormat); i++) {
                                            String s = arrayUserInput[i];
                                            if (!s.startsWith("--p")) {
                                                System.err.println("Error: [--p<txt>] is the correct usage, try again." + s);
                                                correctFormat = false;
                                            }
                                            if (correctFormat){
                                                customTexts.add(s.substring(3));
                                            }
                                        }
                                        if(correctFormat){
                                            ticketAModificar.addProductToTicket(prodId, amount, customTexts);
                                        }
                                    }
                                }
                            } else {
                                System.err.println("Error: Ticket "  + ticketId + " does not exist.");
                            }
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
                if (arrayUserInput.length != 5) {
                    System.err.println("Usage: ticket remove <ticketId> <cashId> <prodId>");
                } else {
                    try {
                        String ticketId = arrayUserInput[2];
                        int cashId = Integer.parseInt(arrayUserInput[3]);
                        int prodId = Integer.parseInt(arrayUserInput[4]);
                        if (Catalog.idExists(prodId)) {
                            Ticket ticketAModificar = ticketManager.getTicketById(ticketId); //Si es null es que no existe dicho ticketId!
                            if(ticketAModificar == null){
                                System.err.println("Error: Ticket " + ticketId + " does not exist.");
                            } else {
                                if(ticketAModificar.getCashId() != cashId){
                                    System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                                }else {
                                    ticketAModificar.removeProductFromTicket(prodId);
                                    System.out.println("ticket remove: ok");
                                }
                            }
                        } else
                            System.out.println("Product with the id " + prodId + " was not found.");
                    } catch (NumberFormatException e) {
                        System.err.println("prodId must be an integer.");
                    } catch (Exception e) {
                        System.err.println("Error removing product from ticket: " + e.getMessage());
                    }
                }
                break;
            case "print":
                try {
                    if(arrayUserInput.length!= 4){
                        System.err.println("Usage: ticket print <ticketId> <cashId>");
                    } else{
                        String ticketId = arrayUserInput[2];
                        int cashId = Integer.parseInt(arrayUserInput[3]);
                        Ticket ticketAMostrar = ticketManager.getTicketById(ticketId);
                        if(ticketAMostrar != null){
                            if(ticketAMostrar.getCashId() != cashId){
                                System.err.println("Error: Ticket " + ticketId + " does not belong to cashier " + cashId);
                            } else{
                                ticketAMostrar.printFinalTicket();
                                System.out.println("ticket print: ok");
                            }
                        } else{
                            System.err.println("Error: Ticket " + ticketId + " does not exist.");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error printing ticket: " + e.getMessage());
                }
                break;
            case "list":
                try{
                    if(arrayUserInput.length!=2){
                        System.err.println("Usage: ticket list");
                    }else{
                        ticketManager.printListTickets();
                        System.out.println("ticket list: ok");
                    }
                } catch (Exception e) {
                    System.err.println("Error printing all ticket: " + e.getMessage());
                }
                break;
            default:
                System.err.println("Unknown ticket command. Type 'help' to see the command list.");
        }
        return ticketManager;
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
                StringBuilder name = new StringBuilder(BasicProduct.getMaxCharName());
                do {
                    i++;
                    if (i != 3)
                        name.append(" ");
                    name.append(arrayUserInput[i]);
                } while (!arrayUserInput[i].endsWith("\""));
                if (name.length() > BasicProduct.getMaxCharName())
                    System.err.println("Maximun " + BasicProduct.getMaxCharName() + " characteres on name");
                else {
                    i++;
                    //"Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC."
                    Category category = Category.valueOf(arrayUserInput[i].toUpperCase());
                    i++;
                    double price = Double.parseDouble(arrayUserInput[i]);
                    BasicProduct product = new BasicProduct(id, name.toString(), category, price);
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
        List<IProduct> productList = Catalog.getCatalog();
        if (productList.isEmpty()) {
            System.out.println("The catalog is empty.");
        } else {
            System.out.println("Catalog:");
           for(IProduct product : productList){
               System.out.println(product.toString());
           }
        }
    }


    private void prodUpdateCommand(String[] arrayUserInput) {
        try {
            boolean updated;
            int id = Integer.parseInt(arrayUserInput[2]);
            IProduct updatedProduct = Catalog.getProduct(id);
            int index = Catalog.indexOfProduct(id);
            if (index != -1 && updatedProduct != null) {
                switch (arrayUserInput[3]) {
                    case "NAME":
                        StringBuilder name = new StringBuilder(BasicProduct.getMaxCharName());
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
            IProduct productRemoved = Catalog.getProduct(id);
            if (productRemoved != null) { //Esto ya directamente comprueba si se puede eliminar o no por lo que no importa no comprobarlo antes
                System.out.print("{class:" + productRemoved.getClass().getSimpleName() +
                        ",id:" + productRemoved.getId() +
                        ",name:" + productRemoved.getName());
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
