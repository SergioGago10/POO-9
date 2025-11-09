package upm.Commands;

import upm.Catalog;
import upm.Products.IProduct;

import java.util.List;

public class ProdListCommand extends Command {

    public ProdListCommand() {
        super("List");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied;
        if (args.length < 2) {
            applied = false;
        } else {
            List<IProduct> catalog = Catalog.getCatalog();
            for (IProduct product : catalog)
                System.out.println(product.toString());
            applied=true;
        }
        return applied;
    }
}

