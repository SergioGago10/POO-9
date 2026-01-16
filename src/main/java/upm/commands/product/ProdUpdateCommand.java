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
        boolean applied = false;
        if (args.length == 5) {
            try {
                String id = args[2];
                ProductManager productManager = ProductManager.getInstance();
                Item product = productManager.getIProduct(id);
                if (product != null) {
                    switch (args[3].toUpperCase()) {
                        case "NAME":
                            if (args[4].length() > ProductManager.MAX_CHAR_NAME)
                                CLI.printErrorNextLine("Error -> Name length must be between 0 and " + ProductManager.MAX_CHAR_NAME);
                            Product productToChange = (Product) (product);
                            productToChange.setName("'" + args[4].trim().replaceAll("^([\"'])|([\"'])$", "") + "'");
                            applied = true;
                            break;
                        case "PRICE":
                            Product productToChange2 = (Product) (product);
                            if (Double.parseDouble(args[4]) < 0)
                                CLI.printErrorNextLine("Error -> Price must be positive");
                            else
                                productToChange2.setPrice(Double.parseDouble(args[4]));
                            applied = true;
                            break;
                        case "CATEGORY":
                            if (product instanceof BasicProduct) {
                                ((BasicProduct) product).setCategory(Category.valueOf(args[4]));
                                applied = true;
                            } else {
                                if (product instanceof ProductService) {
                                    ((ProductService) product).setCategory(ServiceCategory.valueOf(args[4]));
                                    applied = true;
                                }else{
                                    CLI.printErrorNextLine("Error -> That type of product doesn't have category.");
                                }
                            }
                    }
                    if (applied) {
                        CLI.printNextLine(product.toString());
                        CLI.printNextLine("prod update: ok");
                    }
                }
            } catch (IllegalArgumentException ignored) {
                CLI.printErrorNextLine("Error -> Category must be: MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC in Basic/Custom Products," +
                        "or: INSURANCE, TRANSPORT or SHOW in Services");
            }
        }
        return applied;
    }
}
