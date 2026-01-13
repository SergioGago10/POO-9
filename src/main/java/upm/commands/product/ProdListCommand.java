package upm.commands.product;

import upm.CLI;
import upm.commands.core.Command;
import upm.products.Item;
import upm.products.Product;
import upm.products.ProductManager;
import upm.products.ProductService;

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
        ProductManager productManager=ProductManager.getInstance();
        List<Product> catalog = productManager.getCatalogProducts();
        List<ProductService> services=productManager.getCatalogServices();
        if (catalog.isEmpty()&& services.isEmpty()){
            CLI.print("Catalog is empty");
        }else {
            CLI.print("Catalog:");
            for (Item product : catalog) {
                CLI.print(product.toString());
            }
            for (ProductService service : services) {
                CLI.print(service.toString());
            }
            CLI.print("prod list: ok");
        }
        return true;
    }
}

