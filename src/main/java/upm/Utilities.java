package upm;

import upm.Products.BasicProduct;
import upm.Products.ProductManager;

import java.util.Random;

public class Utilities {

    public static void arrayShifterToLeft(BasicProduct[] product) {
        for (int i = 0; i < product.length - 1; i++) {
            if (product[i] == null) {
                for (int j = i; j < product.length - 1; j++) {
                    product[j] = product[j + 1];
                }
                product[product.length - 1] = null;
            }
        }
    }

    public static String generarId() {
        Random random = new Random();
        int num = 1000000 + random.nextInt(9000000);
        return "UW" + num;
    }
    public static boolean isValidProd(int id, String name, double price) {
        boolean resul = true;
        if (id <= 0 ) {
            CLI.print("Id must be positive.");
            return false;
        }
        if (price < 0) {
            CLI.print("Price must be positive.");
            return false;
        }
        if (name.isBlank() || name.length() > ProductManager.MAX_CHAR_NAME) {
            CLI.print("Name length must be between 0 and " + ProductManager.MAX_CHAR_NAME);
            return false;
        }
        return resul;
    }

}
