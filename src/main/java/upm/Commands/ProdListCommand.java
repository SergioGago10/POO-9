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
            applied = true;
            List<IProduct> catalog = Catalog.getCatalog();
            if (catalog.isEmpty())
                CLI.print("Catalog is empty");
            else {
                CLI.print("Catalog:");
                for (IProduct product : catalog)
                    CLI.print(product.toString());
                CLI.print("prod list: ok");
            }
        }
        return applied;
    }
}

