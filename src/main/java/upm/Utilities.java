package upm;

import upm.products.*;

import java.util.*;

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
        return "UW" + randomNumGen(7);
    }

    public static int randomNumGen(int howManyDigits){
        return switch (howManyDigits) {
            case 1 -> new Random().nextInt(10); // 0 a 9
            // return 1 + new Random().nextInt(9); // 1 a 9
            case 2 -> 10 + new Random().nextInt(90); // 10 a 99 - 2 dig
            case 3 -> 100 + new Random().nextInt(900); // 100 a 999 - 3 dig
            case 4 -> 1000 + new Random().nextInt(9000); // 1000 a 99999 - 4 dig
            case 5 -> 10000 + new Random().nextInt(90000); // 10000 a 99999 - 5 dig
            case 6 -> 100000 + new Random().nextInt(900000); // 100000 a 999999 - 6 dig
            case 7 -> 1000000 + new Random().nextInt(9000000); // 1000000 a 9999999 - 7 dig
            case 8 -> 10000000 + new Random().nextInt(90000000); // 10000000 a 99999999 - 8 dig
            case 9 -> 100000000 + new Random().nextInt(900000000); // 100000000 a 999999999 - 9 dig
            default -> -1;
        };
    }


    public static boolean isValidProd(int id, String name, double price) {
        boolean resul = true;
        if (id < 0 ) {
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
