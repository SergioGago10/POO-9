package upm.commands.product;

import upm.CLI;
import upm.commands.core.Command;
import upm.products.Item;
import upm.products.Product;
import upm.products.ProductManager;
import upm.products.ProductService;

import java.util.Comparator;
import java.util.List;

public class ProdListCommand extends Command {

    public ProdListCommand() {
        super("list");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length != 2) {
            CLI.printErrorNextLine("Error -> format must be: prod list");
            return true;
        }

        ProductManager productManager = ProductManager.getInstance();
        List<Product> catalog = productManager.getCatalogProducts();
        List<ProductService> services = productManager.getCatalogServices();

        if (catalog.isEmpty() && services.isEmpty()) {
            CLI.printErrorNextLine("Error -> Catalog is empty");
            return true;
        }

        catalog.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        // Los services no los ordenamos ya que siempre lo van a estar, se ponen productos
        // por orden secuencial siempre.
        CLI.printNextLine("Catalog:");
        for (Item product : catalog) {
            CLI.printNextLine(product.toString());
        }
        for (ProductService service : services) {
            CLI.printNextLine(service.toString());
        }

        CLI.printNextLine("prod list: ok");
        return true;
    }
}

