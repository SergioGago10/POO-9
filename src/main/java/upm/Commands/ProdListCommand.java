package upm.Commands;

import upm.CLI;
import upm.Products.Catalog;
import upm.Products.IProduct;

import java.util.List;

public class ProdListCommand extends Command {

    public ProdListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied;
        if (args.length < 2) {
            CLI.print("Format must be: prod list");
            applied = false;
        } else {
            List<IProduct> catalog = Catalog.getCatalog();
            for (IProduct product : catalog)
                CLI.print(product.toString());
            applied=true;
            CLI.print("prod list: ok");
        }
        return applied;
    }
}

