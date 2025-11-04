package upm;

import upm.Products.BasicProduct;

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

}
