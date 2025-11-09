package upm.Commands;

import upm.Catalog;
import upm.Products.BasicProduct;
import upm.Products.FoodProduct;
import upm.Products.IProduct;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProdRemoveCommand extends Command{
    public ProdRemoveCommand(){
        super("remove");
    }

    @Override
    public boolean apply(String[] args) {
        boolean applied=false;
        if (args.length == 3){
            try {
                int id = Integer.parseInt(args[2]);
                IProduct productRemoved = Catalog.getProduct(id);
                if (productRemoved != null) { //Esto ya directamente comprueba si se puede eliminar o no por lo que no importa no comprobarlo antes
                    System.out.println(productRemoved.toString());
                    if (Catalog.remove(id)) {
                        System.out.println("prod remove: ok");
                        applied=true;
                    }
                } else {
                    System.err.println("The product with the id:" + id + " couldn't be removed. Product not found.");
                }
            } catch (NumberFormatException exception) {
                System.err.print("Id must be an integer number.");
            }
        }
        return applied;
    }
}
