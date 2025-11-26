package upm.Commands;

import upm.CLI;
import upm.Products.Catalog;
import upm.Products.IProduct;

public class ProdRemoveCommand extends Command {
    public ProdRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied = true;
        if (args.length == 3) {
            try {
                int id = Integer.parseInt(args[2]);
                IProduct productRemoved = Catalog.getProduct(id);
                if (productRemoved != null) { //Esto ya directamente comprueba si se puede eliminar o no por lo que no importa no comprobarlo antes
                    Catalog.remove(id);
                    CLI.print(productRemoved.toString());
                    CLI.print("prod remove:ok");
                } else {
                    CLI.print("The product with the id:" + id + " couldn't be removed. Product not found.");
                }
            } catch (NumberFormatException exception) {
                CLI.print("Id must be an integer number.");
                applied = false;
            }
        } else {
            CLI.print("Format must be: prod remove <id>");
            applied = false;
        }
        return applied;
    }
}
