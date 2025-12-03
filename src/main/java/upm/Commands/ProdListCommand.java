package upm.Commands;

import upm.CLI;
import upm.Products.ProductManager;
import upm.Products.Product;

import java.util.List;

public class ProdListCommand extends Command {

    public ProdListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length < 2) {
            CLI.print("Format must be: prod list");
            return true;
        }
        List<Product> catalog = ProductManager.getCatalog();
        if (catalog.isEmpty()){
            CLI.print("Catalog is empty");
            return true;
        }
        CLI.print("Catalog:");
        for (Product product : catalog){
            CLI.print(product.toString());
            CLI.print("prod list: ok");
        }
        return true;
    }
}

