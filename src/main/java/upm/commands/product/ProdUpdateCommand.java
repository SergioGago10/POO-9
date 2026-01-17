package upm.commands.product;

import upm.CLI;
import upm.commands.core.Command;
import upm.products.*;

public class ProdUpdateCommand extends Command {
    public ProdUpdateCommand() {
        super("update");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length != 5) {
            CLI.printErrorNextLine("Error -> Format must be: prod update <id> NAME|CATEGORY|PRICE <value>");
            return true;
        }
        try {
            String id = args[2];
            ProductManager productManager = ProductManager.getInstance();
            Item product = productManager.getIProduct(id);

            if (product == null) {
                CLI.printErrorNextLine("Error -> Product with id: " + id + " does not exist.");
                return true;
            }

            switch (args[3].toUpperCase()) {
                case "NAME":
                    if (args[4].length() > ProductManager.MAX_CHAR_NAME){
                        CLI.printErrorNextLine("Error -> Name length must be between 0 and " + ProductManager.MAX_CHAR_NAME);
                        return true;
                    }
                    Product productToChange = (Product) (product);
                    productToChange.setName("'" + args[4].trim().replaceAll("^([\"'])|([\"'])$", "") + "'");
                    break;
                case "PRICE":
                    Product productToChange2 = (Product) (product);
                    if (Double.parseDouble(args[4]) < 0){
                        CLI.printErrorNextLine("Error -> Price must be positive");
                        return true;
                    }
                    productToChange2.setPrice(Double.parseDouble(args[4]));
                    break;
                case "CATEGORY":
                    try {
                        product.setCategoryFromCLI(args[4]);
                    } catch (UnsupportedOperationException e) {
                        CLI.printErrorNextLine(e.getMessage());
                        return true;
                    } catch (IllegalArgumentException e) {
                        CLI.printErrorNextLine("Error -> Invalid category value.");
                        return true;
                    }
                    break;
            }

            CLI.printNextLine(product.toString());
            CLI.printNextLine("prod update: ok");
        } catch (IllegalArgumentException ignored) {
            CLI.printErrorNextLine("Error -> Category must be: MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC in Basic/Custom Products," +
                    "or: INSURANCE, TRANSPORT or SHOW in Services");
        }
        return true;
    }
}
