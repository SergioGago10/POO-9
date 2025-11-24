package upm.Commands;

import upm.CLI;
import upm.Products.Catalog;
import upm.Products.BasicProduct;
import upm.Products.Category;
import upm.Products.IProduct;

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
                IProduct product;
                product = Catalog.getProduct(id);
                if (product != null) {
                    switch (args[3].toUpperCase()) {
                        case "NAME":
                            product.setName(args[4]);
                            applied = true;
                            break;
                        case "PRICE":
                            product.setPrice(Double.parseDouble(args[4]));
                            applied = true;
                            break;
                        case "CATEGORY":
                            if (product instanceof BasicProduct) {
                                ((BasicProduct) product).setCategory(Category.valueOf(args[4]));
                                applied = true;
                            }
                    }
                }
            } catch (IllegalArgumentException ignored) {
                CLI.print("Category must be MERCH, STATIONERY, CLOTHES, BOOK or ELECTRONIC");
            }
        }
        return applied;
    }
}
