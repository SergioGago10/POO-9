package upm.Commands;

import upm.Catalog;
import upm.Products.BasicProduct;
import upm.Products.Category;
import upm.Products.CustomizableProduct;
import upm.Products.IProduct;

public class ProdAddCommand extends Command {

    public ProdAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied;
        if (args.length < 5) {
            applied = false;
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
                i++;
                category = Category.valueOf(args[i]);
                i++;
                price = Double.parseDouble(args[i]);
                i++;
                if (i == args.length - 1) {
                    int maxPers = Integer.parseInt(args[i]);
                    product = new CustomizableProduct(id, name, category, price, maxPers);
                } else
                    product = new BasicProduct(id, name, category, price);
                Catalog.addProduct(product);
                applied = true;
            } catch (NumberFormatException ex) {
                applied = false;
            }
        }
        return applied;
    }
}
