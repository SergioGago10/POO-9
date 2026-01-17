package upm.commands.product;

import upm.CLI;
import upm.commands.core.Command;
import upm.products.Item;
import upm.products.ProductManager;

public class ProdRemoveCommand extends Command {
    public ProdRemoveCommand() {
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        if (args.length != 3) {
            CLI.printErrorNextLine("Error -> Format must be: prod remove <id>");
            return true;
        }
        try {
            String id = args[2];
            ProductManager productManager = ProductManager.getInstance();
            Item productRemoved = productManager.getIProduct(id);

            if(productRemoved == null) {
                CLI.printErrorNextLine("Error -> The product with the id:" + id + " does not exist.");
                return true;
            }

            if (productManager.remove(id)) { //Esto ya directamente comprueba si se puede eliminar o no por lo que no importa no comprobarlo antes
                CLI.printNextLine(productRemoved.toString());
                CLI.printNextLine("prod remove: ok");
            } else {
                CLI.printErrorNextLine("Error -> The product with the id:" + id + " couldn't be removed.");
            }
            return true;

        } catch (NumberFormatException exception) {
            CLI.printErrorNextLine("Error -> Id must be an integer number.");
            return true;
        }
    }
}
