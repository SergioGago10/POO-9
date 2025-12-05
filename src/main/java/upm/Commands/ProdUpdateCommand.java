package upm.Commands;

import upm.CLI;
import upm.Products.BasicProduct;
import upm.Products.ProductManager;
import upm.Products.Category;
import upm.Products.Product;

public class ProdUpdateCommand extends Command {
    public ProdUpdateCommand() {
        super("update");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = false;
        if (args.length == 5) {
            try {
                int id = Integer.parseInt(args[2]);
                ProductManager productManager=ProductManager.getInstance();
                Product product = productManager.getProduct(id);
                if (product != null) {
                    switch (args[3].toUpperCase()) {
                        case "NAME":
                            if(args[4].length()> ProductManager.MAX_CHAR_NAME)
                                CLI.print("Name length must be between 0 and " + ProductManager.MAX_CHAR_NAME);
                            product.setName(args[4]);
                            applied = true;
                            break;
                        case "PRICE":
                            if (Double.parseDouble(args[4]) < 0)
                                CLI.print("Price must be positive");
                            else
                                product.setPrice(Double.parseDouble(args[4]));
                            applied = true;
                            break;
                        case "CATEGORY":
                            if (product instanceof BasicProduct) {
                                ((BasicProduct) product).setCategory(Category.valueOf(args[4]));
                                applied = true;
                            }
                    }
                    if(applied) {
                        CLI.print(product.toString());
                        CLI.print("prod update: ok");
                    }
                }
            } catch (IllegalArgumentException ignored) {
                CLI.print("Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC");
            }
        }
        return true;
    }
}
