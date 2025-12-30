package upm.Commands;

import upm.CLI;
import upm.Products.Item;
import upm.Products.ProductManager;

public class ProdRemoveCommand extends Command {
    public ProdRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = true;
        if (args.length == 3) {
            try {
                String id =args[2];
                ProductManager productManager=ProductManager.getInstance();
                Item productRemoved = productManager.getIProduct(id);
                if (productRemoved != null && productManager.remove(id)) { //Esto ya directamente comprueba si se puede eliminar o no por lo que no importa no comprobarlo antes
                    CLI.print(productRemoved.toString());
                    CLI.print("prod remove:ok");
                } else {
                    CLI.print("The product with the id:" + id + " couldn't be removed. Product not found.");
                }
            } catch (NumberFormatException exception) {
                CLI.print("Id must be an integer number.");
            }
        } else {
            CLI.print("Format must be: prod remove <id>");
        }
        return applied;
    }
}
