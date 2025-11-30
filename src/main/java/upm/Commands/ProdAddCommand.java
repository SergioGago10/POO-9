package upm.Commands;

import upm.CLI;
import upm.Products.*;
import upm.Utilities;

public class ProdAddCommand extends Command {

    public ProdAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 5) {
            CLI.print("Format must be: prod add [<id>] \"<name>\" <category> <price> [<maxPers>]");
        } else {
            try {
                int i = 2;
                int id;
                String name;
                Category category;
                double price;
                IProduct product;
                if (args[i].contains("\"")) {
                    id = Catalog.generateNewProductId();
                } else {
                    id = Integer.parseInt(args[i]);
                    i++;
                }
                name = args[i].replace("\"", "");
                if (name.length() > Catalog.MAX_CHAR_NAME) {
                    CLI.print("name length must be lower than" + Catalog.MAX_CHAR_NAME);
                    return true;
                }
                i++;
                category = Category.valueOf(args[i]);
                i++;
                price = Double.parseDouble(args[i]);
                if (price < 0) {
                    CLI.print("Price must be positive");
                    return true;
                }
                i++;
                if (Utilities.isValidProd(id, name, price)) {
                    if (i == args.length - 1) {
                        int maxPers = Integer.parseInt(args[i]);
                        product = new CustomizableProduct(id, name, category, price, maxPers);
                    } else
                        product = new BasicProduct(id, name, category, price);
                    if (Catalog.addProduct(product)) {
                        CLI.print(product.toString());
                        CLI.print("prod add:ok");
                    }
                }
            } catch (NumberFormatException ex) {
                CLI.print("Max personalization must be integer and price must be double");
            } catch (IllegalArgumentException exc) {
                CLI.print("Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC");
            }
        }
        return true;
    }
}
