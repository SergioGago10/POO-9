package upm;

import upm.Products.BasicProduct;

import java.util.List;

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
    public static void arrayShifterToLeft(List<Client> clients) {
        for (int i = 0; i < clients.size() - 1; i++) {
            if (clients.get(i) == null) {
                for (int j = i; j < clients.size() - 1; j++) {
                    clients.set(j, clients.get(j + 1));
                }
                clients.set(clients.size() - 1, null);
            }
        }
    }


}
